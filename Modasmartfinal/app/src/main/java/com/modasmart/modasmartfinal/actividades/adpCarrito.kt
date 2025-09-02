package com.modasmart.modasmartfinal.actividades

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.modasmart.modasmartfinal.R
import com.squareup.picasso.Picasso


class adpCarrito(private val carritoList: List<CarritoItems>) :
    RecyclerView.Adapter<adpCarrito.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.lista_carrito, parent, false)
        return CarritoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val currentItem = carritoList[position]

        holder.idProductoCarrito.text = currentItem.id
        holder.nombreProductoCarrito.text = currentItem.nombre
        holder.precioProductoCarrito.text = currentItem.precio.toString()  // Convertir a String

        // Cargar la imagen desde Firebase Storage basándote en la ID del producto
        val storageReference = FirebaseStorage.getInstance().reference
            .child("images/${currentItem.id}.jpg")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .into(holder.imagenProductoCarrito)
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error al cargar la imagen: ${exception.message}")
        }
    }

    override fun getItemCount(): Int {
        return carritoList.size
    }

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idProductoCarrito: TextView = itemView.findViewById(R.id.idProductoCarrito)
        val nombreProductoCarrito: TextView = itemView.findViewById(R.id.nombreProductoCarrito)
        val precioProductoCarrito: TextView = itemView.findViewById(R.id.precioProductoCarrito)
        val imagenProductoCarrito: ImageView = itemView.findViewById(R.id.imageViewProducto)
    }
}

