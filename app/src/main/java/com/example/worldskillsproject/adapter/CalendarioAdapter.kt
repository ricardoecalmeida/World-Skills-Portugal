package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Calendario

class CalendarioAdapter(
    val calendario: ArrayList<Calendario>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<CalendarioAdapter.CalendarioViewHolder>() {
    class CalendarioViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text_data: TextView = ItemView.findViewById(R.id.text_data)
        val text_evento: TextView = ItemView.findViewById(R.id.text_evento)
    }

    class OnClickListener(val clickListener: (calendario: Calendario) -> Unit) {
        fun onClick(calendario: Calendario) = clickListener(calendario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_calendario, parent, false)

        return CalendarioViewHolder(view)
    }
    override fun getItemCount(): Int {
        return calendario.size
    }
    override fun onBindViewHolder(holder: CalendarioViewHolder, position: Int) {
        val calendario = calendario[position]
        holder.text_data.text = calendario.data
        holder.text_evento.text = calendario.evento

        holder.itemView.setOnClickListener {
            onClickListener.onClick(calendario)
        }
    }
}