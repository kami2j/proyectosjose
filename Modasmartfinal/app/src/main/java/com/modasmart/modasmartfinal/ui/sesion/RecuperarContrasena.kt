package com.modasmart.modasmartfinal.ui.sesion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.MainActivity

class RecuperarContrasena : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        val botonVolver = findViewById<Button>(R.id.AtrasButton)
        botonVolver.setOnClickListener{
            val Intent1 = Intent(this, MainActivity::class.java)
            startActivity(Intent1)
        }
        val botonMail = findViewById<Button>(R.id.recumail_button)
        botonMail.setOnClickListener{
            val Intent2 = Intent(this, Mail::class.java)
            startActivity(Intent2)
        }
        val botonLlamada = findViewById<Button>(R.id.recullamada_button)
        botonLlamada.setOnClickListener{
            val Intent3 = Intent(this, Llamada::class.java)
            startActivity(Intent3)
        }
        val botonSMS = findViewById<Button>(R.id.recusms_button)
        botonSMS.setOnClickListener{
            val Intent4 = Intent(this, SMS::class.java)
            startActivity(Intent4)
        }
    }
}