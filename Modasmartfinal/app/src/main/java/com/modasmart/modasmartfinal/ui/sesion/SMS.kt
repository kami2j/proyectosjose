package com.modasmart.modasmartfinal.ui.sesion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.modasmart.modasmartfinal.R

class SMS : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)

        val botonVolver = findViewById<Button>(R.id.AtrasButton)
        botonVolver.setOnClickListener{
            val Intent1 = Intent(this, RecuperarContrasena::class.java)
            startActivity(Intent1)
        }
    }
}