package com.modasmart.modasmartfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.google.firebase.storage.FirebaseStorage

class adpProductoBusqueda(private val empList: ArrayList<ModeloProductoBusqueda>) : RecyclerView.Adapter<adpProductoBusqueda.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lista_producto, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.SujetadorProducto.text = currentEmp.Nombre
        holder.CategoriaProducto1.text = currentEmp.Categoria
        holder.PrecioProducto1.text = "$" +  currentEmp.Precio.toString()

        val storageReference = FirebaseStorage.getInstance().reference
            .child("images/${currentEmp.Id}.jpg")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .into(holder.imageViewOwo)
        }.addOnFailureListener {
        }
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val SujetadorProducto: TextView = itemView.findViewById(R.id.NombreProducto1)
        val CategoriaProducto1: TextView = itemView.findViewById(R.id.CategoriaProducto1)
        val PrecioProducto1: TextView = itemView.findViewById(R.id.PrecioProducto1)
        val imageViewOwo: ImageView = itemView.findViewById(R.id.owo)

        init {
            itemView.setOnClickListener {
                clickListener?.onItemClick(adapterPosition)
            }
        }
    }
}

