package com.modasmart.admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class adpProducto(private val empList: List<ModeloProducto>) :
    RecyclerView.Adapter<adpProducto.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = empList[position]

        holder.nombre.text = currentItem.Nombre
        holder.hiddenId.text = currentItem.Id

        val storageReference = FirebaseStorage.getInstance().reference
            .child("images/${currentItem.Id}.jpg")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .into(holder.imageView)
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error al cargar la imagen: ${exception.message}")
        }
    }


    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View, listener: onItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        val hiddenId: TextView = itemView.findViewById(R.id.hiddenId)
        val imageView: ImageView = itemView.findViewById(R.id.owo)
        val nombre: TextView = itemView.findViewById(R.id.NombreProducto1)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }
    }
}