package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Recomendacoes

class RecomendacoesAdapter (
    val recomendacoes: ArrayList<Recomendacoes>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<RecomendacoesAdapter.RecomendacoesViewHolder>() {
    class RecomendacoesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text_recomendacoes: TextView = ItemView.findViewById(R.id.text_recomendacoes)
    }
    class OnClickListener (val clickListener: (recomendacoes: Recomendacoes) -> Unit) {
        fun onClick(recomendacoes: Recomendacoes) = clickListener(recomendacoes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendacoesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_recomendacoes, parent, false)
        return RecomendacoesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recomendacoes.size
    }

    override fun onBindViewHolder(holder: RecomendacoesViewHolder, position: Int) {
        val recomendacoes = recomendacoes[position]
        holder.text_recomendacoes.text = recomendacoes.descricao

        holder.itemView.setOnClickListener {
            onClickListener.onClick(recomendacoes)
        }
    }
}