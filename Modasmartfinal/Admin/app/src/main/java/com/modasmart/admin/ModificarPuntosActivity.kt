package com.modasmart.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class ModificarPuntosActivity : AppCompatActivity() {

    private lateinit var etNuevoPuntos: EditText
    private lateinit var btnActualizar: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var correoUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_puntos)

        etNuevoPuntos = findViewById(R.id.etNuevoPuntos)
        btnActualizar = findViewById(R.id.btnActualizarPuntos)


        dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")

        correoUsuario = intent.getStringExtra("correoUsuario") ?: ""

        btnActualizar.setOnClickListener {
            buscarUsuarioPorCorreoYActualizarPuntos()
        }

        val correoTextView = findViewById<TextView>(R.id.CorreoUsuario2)
        correoTextView.text = correoUsuario
    }

    private fun buscarUsuarioPorCorreoYActualizarPuntos() {

        if (correoUsuario.isNotEmpty()) {

            val nuevoPuntos = etNuevoPuntos.text.toString()


            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (usuarioSnap in snapshot.children) {
                            val usuario = usuarioSnap.getValue(UserData::class.java)
                            if (usuario != null && usuario.correo == correoUsuario) {

                                usuarioSnap.ref.child("puntos").setValue(nuevoPuntos)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this@ModificarPuntosActivity,
                                                "Puntos actualizados para el usuario con correo: $correoUsuario",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@ModificarPuntosActivity,
                                                "Error al actualizar puntos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                return
                            }
                        }
                        Toast.makeText(
                            this@ModificarPuntosActivity,
                            "Usuario no encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ModificarPuntosActivity,
                        "Error en la búsqueda de usuarios: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@ModificarPuntosActivity,
                "Correo de usuario no válido",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

