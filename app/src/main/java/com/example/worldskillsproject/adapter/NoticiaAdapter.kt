package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Noticia

class NoticiaAdapter (
    val noticia : ArrayList<Noticia>,
    val onClickListener : OnClickListener
) : RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder>() {
    class NoticiaViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView) {
        val text_titulo: TextView = ItemView.findViewById(R.id.text_noticia_titulo)
        val text_data: TextView = ItemView.findViewById(R.id.text_noticia_data)
        val imagem_noticia: ImageView = ItemView.findViewById(R.id.image_noticias)
        val text_conteudo: TextView = ItemView.findViewById(R.id.text_noticia_conteudo)
    }
    class OnClickListener (val clickListener: (noticia : Noticia) -> Unit) {
        fun onClick(noticia: Noticia) = clickListener(noticia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_noticia, parent, false)
        return NoticiaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noticia.size
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = noticia[position]
        holder.text_titulo.text = noticia.titulo
        holder.text_data.text = noticia.data
        holder.text_conteudo.text = noticia.conteudo

        Glide.with(holder.itemView.context)
            .load(noticia.imagem)
            .into(holder.imagem_noticia)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(noticia)
        }
    }
}
