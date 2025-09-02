package com.modasmart.modasmartfinal.ui.carrito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.modasmart.modasmartfinal.ModeloProducto
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.actividades.CarritoItems
import com.modasmart.modasmartfinal.actividades.adpCarrito
import com.modasmart.modasmartfinal.adpProducto
import com.modasmart.modasmartfinal.databinding.FragmentCarritoBinding
import com.modasmart.modasmartfinal.databinding.FragmentCuentaBinding

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private lateinit var carritoRecyclerView: RecyclerView
    private lateinit var carritoList: ArrayList<CarritoItems>
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: FirebaseStorage
    private lateinit var totalTextView: TextView
    private lateinit var cuentaPuntos: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var puntosUsuario: Long = 0
    private var uidDelUsuario: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)

        val buttonPuntos: Button = view.findViewById(R.id.buttonPuntos)
        carritoRecyclerView = view.findViewById(R.id.carritoRV)
        carritoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        carritoRecyclerView.setHasFixedSize(true)

        totalTextView = view.findViewById(R.id.TotalPrecio)

        carritoList = arrayListOf()
        storageRef = FirebaseStorage.getInstance()

        getCarritoData()

        buttonPuntos.setOnClickListener {
            applyDescuento()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cuentaPuntos = view.findViewById(R.id.totalPuntos)

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")
        auth = FirebaseAuth.getInstance()

        val correoAuth = auth.currentUser?.email

        if (correoAuth != null) {
            val consulta = databaseReference.orderByChild("correo").equalTo(correoAuth)
            consulta.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (usuarioSnapshot in dataSnapshot.children) {
                            val correoUsuario = usuarioSnapshot.child("correo").getValue(String::class.java)
                            puntosUsuario = usuarioSnapshot.child("puntos").getValue(Long::class.java) ?: 0

                            if (correoUsuario != null) {
                                if (correoUsuario == correoAuth) {
                                    cuentaPuntos.text = "Puntos:$puntosUsuario"
                                } else {
                                    cuentaPuntos.text = "No se han encontrado Puntos"
                                }
                            }
                        }
                    } else {
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }

    private fun applyDescuento() {
        // Obtén el valor total actual
        val totalString = totalTextView.text.toString().removePrefix("Total: ")
        val precioTotal = totalString.toLongOrNull() ?: 0

        // Verifica si el valor total es mayor o igual a 1000
        if (precioTotal < 1000) {
            Toast.makeText(requireContext(), "El valor total no puede ser inferior a 1000", Toast.LENGTH_SHORT).show()
            return
        }

        // Pregunta al usuario si desea utilizar puntos
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("¿Deseas utilizar puntos?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario eligió utilizar puntos
            val puntosString = cuentaPuntos.text.toString().removePrefix("Puntos:")
            val puntos = puntosString.toLongOrNull() ?: 0

            // Calcula el descuento basado en el valor total y los puntos disponibles
            val descuento = minOf(precioTotal - 1000, puntos * 10)
            val precioConDescuento = maxOf(1000, precioTotal - descuento)

            // Actualiza el TextView con el nuevo total
            totalTextView.text = "Total: $precioConDescuento"

            // Resta los puntos utilizados del total de puntos
            puntosUsuario -= descuento / 10 // 1 punto equivale a 10 pesos

            // Actualiza el TextView de puntos en la interfaz
            cuentaPuntos.text = "Puntos: $puntosUsuario"

            // Actualiza los puntos en la base de datos
            updatePuntosEnFirebase(puntosUsuario)
        }
        builder.setNegativeButton("No") { _, _ ->
            // Usuario eligió no utilizar puntos
            // Aquí puedes realizar acciones adicionales si es necesario
        }

        val dialog = builder.create()
        dialog.show()
    }




    private fun updatePuntosEnFirebase(puntosRestantes: Long) {
        val auth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")

        val usuarioActual = auth.currentUser

        if (usuarioActual != null) {
            val correoUsuario = usuarioActual.email

            // Encuentra al usuario en la base de datos utilizando el correo
            val query = databaseReference.orderByChild("correo").equalTo(correoUsuario)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (usuarioSnapshot in dataSnapshot.children) {
                            // Obtén el ID del usuario
                            val usuarioId = usuarioSnapshot.key

                            // Actualiza los puntos del usuario con los puntos restantes
                            databaseReference.child(usuarioId!!).child("puntos")
                                .setValue(puntosRestantes)
                        }
                    } else {
                        // El usuario no fue encontrado
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores
                }
            })
        } else {
            // El usuario no está autenticado
            // Puedes redirigirlo a la pantalla de inicio de sesión o realizar alguna acción adecuada
        }
    }






    private fun getCarritoData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Carrito")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                carritoList.clear()
                var total: Long = 0

                if (snapshot.exists()) {
                    for (carritoSnap in snapshot.children) {
                        val carritoItem = carritoSnap.getValue(CarritoItems::class.java)
                        carritoItem?.let {
                            carritoList.add(it)
                            // Suma el precio de cada producto al total
                            total += it.precio
                        }
                    }

                    // Adaptador para el carrito utilizando la lista de CarritoItems
                    val mAdapter = adpCarrito(carritoList)
                    carritoRecyclerView.adapter = mAdapter

                    // Actualiza el TextView con el total calculado
                    totalTextView.text = "Total: $total"
                } else {
                    // Handle the case when there are no items in the carrito
                    // Puedes mostrar un mensaje o manejarlo según tus necesidades
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja errores de base de datos
                // Puedes mostrar un mensaje o manejarlo según tus necesidades
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
