package com.modasmart.modasmartfinal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.modasmart.modasmartfinal.ui.sesion.Mail
import com.modasmart.modasmartfinal.ui.sesion.RecuperarContrasena
import com.modasmart.modasmartfinal.ui.sesion.Registrar

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)


        val botonRecuperar = findViewById<Button>(R.id.recuperar_contra_button)
        botonRecuperar.setOnClickListener{
            val intent2 = Intent(this, Mail::class.java)
            startActivity(intent2)
        }
        val botonRegistrar = findViewById<Button>(R.id.registrarseButton)
        botonRegistrar.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }


        setup()
        session()

    }


    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null ){

            val pantalla = findViewById<ConstraintLayout>(R.id.pantallaLogin)
            pantalla.visibility = View.INVISIBLE
            home(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup(){

        title="Iniciar Sesión"

        val iniciarSesion : Button = findViewById<Button>(R.id.loginButton)
        val correo : EditText = findViewById<EditText>(R.id.iniciarCorreo)
        val contra : EditText = findViewById<EditText>(R.id.iniciarContrasena)


        iniciarSesion.setOnClickListener {
            if(correo.text.isNotEmpty() && contra.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo.text.toString(),contra.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        home(it.result?.user?.email?:"", ProviderType.BASIC)

                    }else{
                        alerta()
                    }
                }
            }
        }
    }
    private fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se ha encontrado una cuenta con los datos ingresados. Verifique si el correo o contraseña están ingresados correctamente")
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
    }

}