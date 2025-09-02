package com.modasmart.admin

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.squareup.picasso.Picasso

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var Id: TextView
    private lateinit var Nombre: TextView
    private lateinit var Descripcion: TextView
    private lateinit var Categoria: TextView
    private lateinit var Color: TextView
    private lateinit var Precio: TextView
    private lateinit var Img: ImageView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("Id").toString(),
                intent.getStringExtra("Nombre").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("Id").toString()
            )
        }
    }

    private fun openUpdateDialog(
        Id: String,
        Nombres: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val actNombre = mDialogView.findViewById<EditText>(R.id.actNombre)
        val actDescripcion = mDialogView.findViewById<EditText>(R.id.actDescripcion)
        val actCategoria = mDialogView.findViewById<EditText>(R.id.actCategoria)
        val actColor = mDialogView.findViewById<EditText>(R.id.actColor)
        val actPrecio = mDialogView.findViewById<EditText>(R.id.actValor)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        actNombre.setText(intent.getStringExtra("Nombre").toString())
        actDescripcion.setText(intent.getStringExtra("Descripcion").toString())
        actCategoria.setText(intent.getStringExtra("Categoria").toString())
        actColor.setText(intent.getStringExtra("Color").toString())
        actPrecio.setText(intent.getLongExtra("Precio", 0).toString())

        mDialog.setTitle("Updating $Nombres Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                Id,
                actNombre.text.toString(),
                actDescripcion.text.toString(),
                actCategoria.text.toString(),
                actColor.text.toString(),
                actPrecio.text.toString().toLong()
            )

            Toast.makeText(applicationContext, "La información del producto ha sido actualizada", Toast.LENGTH_LONG).show()

            // Actualizar los TextViews
            Nombre.text = actNombre.text.toString()
            Descripcion.text = actDescripcion.text.toString()
            Categoria.text = actCategoria.text.toString()
            Color.text = actColor.text.toString()
            Precio.text = actPrecio.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        Id: String,
        Nombre: String,
        Descripcion: String,
        Categoria: String,
        Color: String,
        Precio: Long
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Productos").child(Id)
        val empInfo = ModeloProducto(Id, Nombre, Descripcion, Categoria, Color, Precio)
        dbRef.setValue(empInfo)
    }

    private fun initView() {
        Id = findViewById(R.id.Id)
        Nombre = findViewById(R.id.Nombre)
        Descripcion = findViewById(R.id.Descripcion)
        Categoria = findViewById(R.id.Categoria)
        Color = findViewById(R.id.Color)
        Precio = findViewById(R.id.Precio)
        Img = findViewById(R.id.foto)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        Id.text = intent.getStringExtra("Id")
        Nombre.text = intent.getStringExtra("Nombre")
        Descripcion.text = intent.getStringExtra("Descripcion")
        Categoria.text = intent.getStringExtra("Categoria")
        Color.text = intent.getStringExtra("Color")
        Precio.text = intent.getLongExtra("Precio", 0).toString()

        val imageId = intent.getStringExtra("Id")
        val storageRef = FirebaseStorage.getInstance().getReference("images/$imageId.jpg")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
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

    private fun deleteRecord(
        Id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Productos").child(Id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "El producto ha sido eliminado", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error al eliminar ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
