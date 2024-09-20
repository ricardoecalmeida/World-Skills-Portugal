package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Utilizador

class UtilizadorAdapter (
    val utilizador: ArrayList<Utilizador>,
    val onClickListener: OnClickListener
): RecyclerView.Adapter<UtilizadorAdapter.UtilizadorViewHolder>() {

    class UtilizadorViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text_nome: TextView = ItemView.findViewById(R.id.text_nome_user)
        val text_instituicao: TextView = ItemView.findViewById(R.id.text_instituicao)
        val image_user: ImageView = ItemView.findViewById(R.id.image_user)
    }

    class OnClickListener(val clickListener: (utilizador: Utilizador) -> Unit) {
        fun onClick (utilizador: Utilizador) = clickListener(utilizador)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilizadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_calendario, parent, false)
        return UtilizadorViewHolder(view)
    }
    override fun getItemCount(): Int {
        return utilizador.size
    }

    override fun onBindViewHolder(holder: UtilizadorViewHolder, position: Int) {
        val utilizador = utilizador[position]
        holder.text_nome.text = utilizador.nome
        holder.text_instituicao.text = utilizador.instituicao

        Glide.with(holder.itemView.context)
            .load(utilizador.foto)
            .into(holder.image_user)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(utilizador)
        }
    }
}
