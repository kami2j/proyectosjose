package com.modasmart.modasmartfinal

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.modasmart.modasmartfinal.actividades.FirebaseDBManager
import com.squareup.picasso.Picasso

class Producto : AppCompatActivity() {
    private lateinit var Id: TextView
    private lateinit var Nombre: TextView
    private lateinit var Descripcion: TextView
    private lateinit var Categoria: TextView
    private lateinit var Color: TextView
    private lateinit var Precio: TextView
    private lateinit var Img: ImageView
    private lateinit var buttonComprar: Button
    private lateinit var databaseManager: FirebaseDBManager // Agregado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        initView()
        setValuesToViews()

        // Inicializa la referencia a la base de datos
        databaseManager = FirebaseDBManager()

        // Inicializa el botón y agrega un OnClickListener para manejar la acción de compra
        buttonComprar.setOnClickListener {
            addToCart()
        }
    }

    private fun initView() {
        Id = findViewById(R.id.Id)
        Nombre = findViewById(R.id.Nombre)
        Descripcion = findViewById(R.id.Descripcion)
        Categoria = findViewById(R.id.Categoria)
        Color = findViewById(R.id.Color)
        Precio = findViewById(R.id.Precio)
        Img = findViewById(R.id.foto)
        buttonComprar = findViewById(R.id.buttonComprar)
    }

    private fun setValuesToViews() {
        Id.text = intent.getStringExtra("Id")
        Nombre.text = intent.getStringExtra("Nombre")
        Descripcion.text = intent.getStringExtra("Descripcion")
        Categoria.text = intent.getStringExtra("Categoria")
        Color.text = intent.getStringExtra("Color")
        Precio.text = "$" + intent.getLongExtra("Precio", 0)

        val imageId = intent.getStringExtra("Id")
        val storageRef = FirebaseStorage.getInstance().getReference("images/$imageId.jpg")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            // Utiliza Picasso para cargar la imagen desde la URL
            Picasso.get().load(uri).into(Img)
        }.addOnFailureListener { exception ->
            if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                Toast.makeText(
                    this,
                    "No se encontró la imagen en Firebase Storage",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Error al cargar la imagen: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addToCart() {
        val productIdToAdd = intent.getStringExtra("Id")
        val productName = intent.getStringExtra("Nombre")
        val productPrice = intent.getLongExtra("Precio", 0)

        if (productIdToAdd != null && productName != null && productPrice != null) {
            // Sube el producto al carrito con el precio como String
            databaseManager.addItemToCart(productIdToAdd, productName, productPrice)
            Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
        } else {
            // Manejar el caso cuando no se puedan obtener los valores
            Toast.makeText(this, "Error al obtener información del producto", Toast.LENGTH_SHORT)
                .show()
        }
    }
}