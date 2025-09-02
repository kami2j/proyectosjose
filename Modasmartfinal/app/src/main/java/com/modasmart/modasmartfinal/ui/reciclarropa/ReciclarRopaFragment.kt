package com.modasmart.modasmartfinal.ui.reciclarropa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.databinding.FragmentReciclarRopaBinding
import com.modasmart.modasmartfinal.ui.DatosUsuarios

class ReciclarRopaFragment : Fragment() {

    private var _binding: FragmentReciclarRopaBinding? = null
    private val binding get() = _binding!!
    private lateinit var donNombre : TextView
    private lateinit var donTelefono : TextView
    private lateinit var donCorreo : TextView
    private lateinit var donCantidad : TextView
    private lateinit var btnSuma : Button
    private lateinit var btnResta : Button
    private var cantidad : Int = 1
    private lateinit var  dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_reciclar_ropa, container, false)
        _binding = FragmentReciclarRopaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        donNombre = view.findViewById(R.id.donNombre)
        donTelefono = view.findViewById(R.id.donNCelular)
        donCorreo = view.findViewById(R.id.donCorreo)
        donCantidad = view.findViewById(R.id.donCantidad)
        btnSuma = view.findViewById(R.id.buttonSuma)
        btnResta = view.findViewById(R.id.buttonResta)
        dbRef = FirebaseDatabase.getInstance().getReference("Donacion")

        val bundle = activity?.intent?.extras
        val email = bundle?.getString("email")
        donCorreo.text = email

        binding.buttonSuma.setOnClickListener {
            sumarCantidad()
        }
        binding.buttonResta.setOnClickListener{
            restarCantidad()
        }

        binding.buttonEnviar.setOnClickListener {
            donarRopa()
        }

    }

    fun sumarCantidad(){
        if(cantidad < 10){
            cantidad++
            donCantidad.text = cantidad.toString()
        }else{

        }
    }
    fun restarCantidad(){
        if(cantidad > 1){
            cantidad--
            donCantidad.text = cantidad.toString()
        }else{

        }
    }

    fun donarRopa(){
        var Nombre = donNombre.text.toString()
        var Telefono = donTelefono.text.toString()
        var Correo = donCorreo.text.toString()
        var Cantidad = donCantidad.text.toString()

        val id = dbRef.push().key!!
        val donacion = DatosDonacion(Nombre, Telefono, Correo, Cantidad)

        dbRef.child(id).setValue(donacion).addOnCompleteListener{
            mostrarAlerta()
            binding.donCantidad.text = "1"
            binding.donNCelular.text = null
            binding.donNombre.text = null
        }

    }

    private fun mostrarAlerta() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Donación Enviada")
        builder.setMessage("Tu donación ha sido enviada para su aprobación. Muchas Gracias")
        builder.setPositiveButton("Cerrar", null)
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}