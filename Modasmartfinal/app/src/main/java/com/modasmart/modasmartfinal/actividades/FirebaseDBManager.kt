package com.modasmart.modasmartfinal.actividades

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseDBManager {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun addItemToCart(productoId: String?, productName: String?, productoPrecio: Long?) {
        if (productoId != null && productName != null && productoPrecio != null) {
            val carritoItems = CarritoItems(productoId, productName, productoPrecio)
            database.child("Carrito").child(productoId).setValue(carritoItems)
        }
    }

    fun getCartItems(callback: (List<CarritoItems>) -> Unit) {
        database.child("Carrito").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val carritoItems = mutableListOf<CarritoItems>()
                for (itemSnapshot in snapshot.children) {
                    val carritoItem = itemSnapshot.getValue(CarritoItems::class.java)
                    carritoItem?.let { carritoItems.add(it) }
                }
                callback(carritoItems)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
