package com.modasmart.admin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class adpReciclaje(private val recList: List<ReciclajeData>) :
    RecyclerView.Adapter<adpReciclaje.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_list_reciclaje, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = recList[position]

        holder.Nombretv.text = currentItem.nombre
        holder.Correotv.text = currentItem.correo

    }

    override fun getItemCount(): Int {
        return recList.size
    }

    class ViewHolder(itemView: View, listener: onItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        val Nombretv : TextView = itemView.findViewById(R.id.nombreReciclaje)
        val Correotv: TextView = itemView.findViewById(R.id.correoReciclaje)

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