package com.modasmart.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReciclajeDetalles : AppCompatActivity() {
    private lateinit var btnPuntos: Button
    private lateinit var nombre: String
    private lateinit var correo: String
    private lateinit var cantidad: String
    private lateinit var telefono: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_donaciones)

        val bundle = intent.extras

        nombre = bundle?.getString("RecNombre").toString()
        correo = bundle?.getString("RecCorreo").toString()
        cantidad = bundle?.getString("RecCantidad").toString()
        telefono = bundle?.getString("RecTelefono").toString()

        btnPuntos = findViewById(R.id.btnPuntos)


        btnPuntos.setOnClickListener {
            val intent = Intent(this@ReciclajeDetalles,ModificarPuntosActivity::class.java)
            startActivity(intent)
        }
        val nombreTV = findViewById<TextView>(R.id.recNombre)
        val correoTV = findViewById<TextView>(R.id.recCorreo)
        val cantidadTV = findViewById<TextView>(R.id.recCantidad)
        val telefonoTV = findViewById<TextView>(R.id.recTelefono)

        nombreTV.text = nombre
        correoTV.text = correo
        cantidadTV.text = cantidad
        telefonoTV.text = telefono
    }
}