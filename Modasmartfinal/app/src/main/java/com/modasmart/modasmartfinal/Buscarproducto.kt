package com.modasmart.modasmartfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class Buscarproducto : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<ModeloProducto>
        private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: FirebaseStorage
    private lateinit var categoria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscarproducto)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<ModeloProducto>()


        storageRef = FirebaseStorage.getInstance()

        categoria = intent.getStringExtra("categoria") ?: ""

        if (categoria.isNotEmpty()) {
            getEmployeesData()
        } else {
        }
    }

    private fun getEmployeesData() {
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Productos")

        dbRef.orderByChild("categoria").equalTo("$categoria")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    empList.clear()
                    if (snapshot.exists()) {
                        for (empSnap in snapshot.children) {
                            val empData = empSnap.getValue(ModeloProducto::class.java)
                            empList.add(empData!!)
                        }

                        val mAdapter = adpProducto(empList)
                        empRecyclerView.adapter = mAdapter

                        mAdapter.setOnItemClickListener(object : adpProducto.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@Buscarproducto, Producto::class.java)
                                intent.putExtra("Id", empList[position].Id)
                                intent.putExtra("Nombre", empList[position].Nombre)
                                intent.putExtra("Descripcion", empList[position].Descripcion)
                                intent.putExtra("Categoria", empList[position].Categoria)
                                intent.putExtra("Color", empList[position].Color)
                                intent.putExtra("Precio", empList[position].Precio)
                                
                                val imageId = intent.getStringExtra("Id")
                                val storageReference = storageRef.reference
                                    .child("images/$imageId.jpg")

                                storageReference.downloadUrl.addOnSuccessListener { uri ->
                                    intent.putExtra("ImagenUri", uri.toString())
                                    startActivity(intent)
                                }.addOnFailureListener {
                                }
                            }
                        })

                        empRecyclerView.visibility = View.VISIBLE
                        tvLoadingData.visibility = View.GONE
                    } else {
                        empRecyclerView.visibility = View.GONE
                        tvLoadingData.visibility = View.VISIBLE
                        tvLoadingData.text = "No hay productos para la categoría: $categoria"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores de base de datos
                }
            })
    }
}