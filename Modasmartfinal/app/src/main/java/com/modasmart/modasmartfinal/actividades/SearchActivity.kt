package com.modasmart.modasmartfinal.actividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.modasmart.modasmartfinal.R
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.modasmart.modasmartfinal.ModeloProductoBusqueda
import com.modasmart.modasmartfinal.Producto
import com.modasmart.modasmartfinal.adpProductoBusqueda

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: adpProductoBusqueda
    private val productoList = ArrayList<ModeloProductoBusqueda>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Buscar Productos"

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = adpProductoBusqueda(productoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                if (!query.isNullOrBlank()) {
                    buscarEnFirebase(query)
                } else {
                    productoList.clear()
                    adapter.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    buscarEnFirebase(newText)
                } else {
                    productoList.clear()
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })

        adapter.setOnItemClickListener(object : adpProductoBusqueda.onItemClickListener {
            override fun onItemClick(position: Int) {

                val selectedProduct = productoList[position]

                val intent = Intent(this@SearchActivity, Producto::class.java).apply {
                    putExtra("Id", selectedProduct.Id)
                    putExtra("Nombre", selectedProduct.Nombre)
                    putExtra("Descripcion", selectedProduct.Descripcion)
                    putExtra("Categoria", selectedProduct.Categoria)
                    putExtra("Color", selectedProduct.Color)
                    putExtra("Precio", selectedProduct.Precio)

                }

                startActivity(intent)
            }
        })
    }

    private fun buscarEnFirebase(query: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Productos")


        productoList.clear()


        databaseReference.orderByChild("nombre").startAt(query).endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val producto = data.getValue(ModeloProductoBusqueda::class.java)
                        producto?.let { productoList.add(it) }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        databaseReference.orderByChild("categoria").startAt(query).endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val producto = data.getValue(ModeloProductoBusqueda::class.java)
                        producto?.let { productoList.add(it) }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        databaseReference.orderByChild("color").startAt(query).endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val producto = data.getValue(ModeloProductoBusqueda::class.java)
                        producto?.let { productoList.add(it) }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })



        adapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


