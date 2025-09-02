package com.modasmart.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReciclajeActivity: AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var recList: ArrayList<ReciclajeData>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_reciclaje)

        empRecyclerView = findViewById(R.id.rvRec)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        recList = arrayListOf<ReciclajeData>()

        getEmployeesData()
    }

    private fun getEmployeesData() {
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Donacion")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(ReciclajeData::class.java)
                        recList.add(empData!!)
                    }

                    val mAdapter = adpReciclaje(recList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : adpReciclaje.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ReciclajeActivity, ReciclajeDetalles::class.java)

                            intent.putExtra("RecNombre", recList[position].nombre)
                            intent.putExtra("RecCantidad", recList[position].cantidad)
                            intent.putExtra("RecCorreo", recList[position].correo)
                            intent.putExtra("RecTelefono", recList[position].telefono)

                            startActivity(intent)
                        }
                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar la cancelación (si es necesario)
                Toast.makeText(this@ReciclajeActivity, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}