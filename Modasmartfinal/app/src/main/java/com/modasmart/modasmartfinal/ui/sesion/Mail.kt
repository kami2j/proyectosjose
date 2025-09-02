package com.modasmart.modasmartfinal.ui.sesion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.modasmart.modasmartfinal.MainActivity
import com.modasmart.modasmartfinal.R

class Mail : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var correoRec : EditText
    private lateinit var enviarCorreo : Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var correo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        val botonVolver = findViewById<Button>(R.id.AtrasButton)
        botonVolver.setOnClickListener{
            val Intent1 = Intent(this, MainActivity::class.java)
            startActivity(Intent1)
        }
        auth = FirebaseAuth.getInstance()

        correoRec = findViewById(R.id.recuperarCorreo)
        enviarCorreo = findViewById(R.id.EnviarMailRecu)

        enviarCorreo.setOnClickListener {
            correo= correoRec.text.toString()
            auth.sendPasswordResetEmail(correo).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Hemos enviaado un correo para reestablecer su contraseña.",
                    Toast.LENGTH_SHORT
                ).show()


            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }

}