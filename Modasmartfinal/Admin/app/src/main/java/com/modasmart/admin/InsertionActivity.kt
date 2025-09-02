package com.modasmart.admin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.modasmart.admin.databinding.ActivityInsertionBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask



class InsertionActivity : AppCompatActivity() {
    private lateinit var etEmpNombre: EditText
    private lateinit var etEmpDescripcion: EditText
    private lateinit var etEmpCategoria: EditText
    private lateinit var etEmpColor: EditText
    private lateinit var etEmpPrecio: EditText
    private lateinit var btnIngresar: Button

    private lateinit var binding: ActivityInsertionBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance().reference

        etEmpNombre = findViewById(R.id.etEmpNombre)
        etEmpDescripcion = findViewById(R.id.etEmpDescripcion)
        etEmpCategoria = findViewById(R.id.etEmpCategoria)
        etEmpColor = findViewById(R.id.etEmpColor)
        etEmpPrecio = findViewById(R.id.etEmpPrecio)
        btnIngresar = findViewById(R.id.btnIngresar)
        dbRef = FirebaseDatabase.getInstance().getReference("Productos")

        btnIngresar.setOnClickListener {
            guardarDatosProducto()
        }

        binding.SubirIMG.setOnClickListener {
            selectImage()
        }
    }

    private fun guardarDatosProducto() {
        val Nombre = binding.etEmpNombre.text.toString()
        val Descripcion = binding.etEmpDescripcion.text.toString()
        val Categoria = binding.etEmpCategoria.text.toString()
        val Color = binding.etEmpColor.text.toString()
        val Precio = binding.etEmpPrecio.text.toString()

        if (Nombre.isEmpty()) {
            etEmpNombre.error = "Por favor ingrese el nombre"
            return
        }
        if (Descripcion.isEmpty()) {
            etEmpDescripcion.error = "Por favor inserte la descripción"
            return
        }
        if (Categoria.isEmpty()) {
            etEmpCategoria.error = "Por favor inserte la Categoria"
            return
        }
        if (Color.isEmpty()) {
            etEmpColor.error = "Por favor inserte el color"
            return
        }
        if (Precio.isEmpty()) {
            etEmpPrecio.error = "Por favor inserte el precio"
            return
        }

        // Convertir el precio a Long
        val precioLong: Long
        try {
            precioLong = Precio.toLong()
        } catch (e: NumberFormatException) {
            etEmpPrecio.error = "Formato de precio no válido"
            return
        }

        if (::ImageUri.isInitialized) {
            val Id = dbRef.push().key!!
            val imageRef = storageRef.child("images/$Id.jpg")
            val uploadTask: UploadTask = imageRef.putFile(ImageUri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val producto = ModeloProducto(Id, Nombre, Descripcion, Categoria, Color, precioLong, downloadUri.toString())

                    dbRef.child(Id).setValue(producto)
                        .addOnCompleteListener {
                            Toast.makeText(this, "La prenda ha sido agregada exitosamente", Toast.LENGTH_LONG).show()

                            etEmpNombre.text.clear()
                            etEmpDescripcion.text.clear()
                            etEmpCategoria.text.clear()
                            etEmpColor.text.clear()
                            etEmpPrecio.text.clear()
                            binding.imageViewuwu.setImageResource(android.R.color.transparent)
                        }
                } else {
                    Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.imageViewuwu.setImageURI(ImageUri)
        }
    }
}
