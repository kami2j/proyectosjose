package com.modasmart.modasmartfinal.ui.sesion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.modasmart.modasmartfinal.MainActivity
import com.modasmart.modasmartfinal.MainActivity2
import com.modasmart.modasmartfinal.ProviderType
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.actividades.TyC
import com.modasmart.modasmartfinal.ui.DatosUsuarios

class Registrar : ComponentActivity() {


    private lateinit var nombre : EditText
    private lateinit var  correo : EditText
    private lateinit var  contra : EditText
    private lateinit var  fono : EditText
    private lateinit var  tyc : CheckBox
    private var  puntos : Int = 0
    private lateinit var  dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        val crearCuenta = findViewById<Button>(R.id.buttonCrearCuenta)
        nombre  = findViewById<EditText>(R.id.ingresarNombre)
        correo = findViewById<EditText>(R.id.ingresarCorreo)
        contra = findViewById<EditText>(R.id.ingresarContra)
        fono = findViewById<EditText>(R.id.ingresarTelefono)
        tyc = findViewById<CheckBox>(R.id.aceptarTerminos)
        dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")

        val botonVolver = findViewById<Button>(R.id.AtrasButton)
        botonVolver.setOnClickListener{
            val Intent1 = Intent(this, MainActivity::class.java)
            startActivity(Intent1)
        }

        val botonTyC = findViewById<Button>(R.id.buttonTerminos)
        botonTyC.setOnClickListener {
            val Intent2 = Intent(this, TyC::class.java)
            startActivity(Intent2)
        }

        title="Registrar Usuario"

        crearCuenta.setOnClickListener {
            if(nombre.text.isNotEmpty() && correo.text.isNotEmpty() && contra.text.isNotEmpty() && fono.text.isNotEmpty() && tyc.isChecked){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.text.toString(),contra.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        home(it.result?.user?.email?:"", ProviderType.BASIC)

                    }else{
                        alerta()
                    }
                }
            }else{

            }
        }

    }

    private fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar los datos. Lamentamos las molestias")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun home(email: String, provider: ProviderType){
        val homeIntent = Intent(this, MainActivity2::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        guardarDatos()
    }

    private fun guardarDatos(){
        val Nombre = nombre.text.toString()
        val Correo = correo.text.toString()
        val Telefono = fono.text.toString()
        val Contrasena = contra.text.toString()
        val Puntos = puntos.toLong()

        val id = dbRef.push().key!!
        val usuario = DatosUsuarios(Nombre, Correo, Contrasena, Telefono, Puntos)

        dbRef.child(id).setValue(usuario).addOnCompleteListener{
            nombre.text.clear()
            correo.text.clear()
            fono.text.clear()
            contra.text.clear()
        }


    }

}