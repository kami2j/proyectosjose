package com.modasmart.modasmartfinal.ui.cuenta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.modasmart.modasmartfinal.MainActivity
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.databinding.FragmentCuentaBinding

class CuentaFragment : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var cuentaCorreo : TextView
    private lateinit var cuentaNombre : TextView
    private lateinit var cuentaPuntos : TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_cuenta, container, false)
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cuentaCorreo = view.findViewById(R.id.textCorreoUsuario)
        cuentaNombre = view.findViewById(R.id.textNombreUsuario)
        cuentaPuntos = view.findViewById(R.id.textPuntos)

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
                            val nombreUsuario = usuarioSnapshot.child("nombre").getValue(String::class.java)
                            val puntosUsuario = usuarioSnapshot.child("puntos").getValue(Long::class.java)

                            if (correoUsuario != null) {
                                if (correoUsuario == correoAuth) {
                                    cuentaCorreo.text = correoUsuario
                                    cuentaNombre.text = nombreUsuario
                                    cuentaPuntos.text = puntosUsuario.toString()

                                } else {
                                    cuentaCorreo.text = "Dato no coincide"
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

        binding.buttonCerrarSesion.setOnClickListener {
            val prefs = getActivity()?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
            prefs?.clear()
            prefs?.apply()

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}