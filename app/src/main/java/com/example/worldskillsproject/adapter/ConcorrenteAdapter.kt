package com.example.worldskillsproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Concorrente

class ConcorrenteAdapter (val listaConcorrentes:ArrayList<Concorrente>, val onClickListener:ItemClickListener) : RecyclerView.Adapter<ConcorrenteAdapter.ConcorrenteViewHolder>() {



    class ConcorrenteViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image: ImageView = itemView.findViewById(R.id.image_concorrente_profissao)
        val nome: TextView = itemView.findViewById((R.id.text_nome_concorrente))



        fun bind(concorrente: Concorrente, onClickListener: ConcorrenteAdapter.ItemClickListener) {
            Log.d("ConcorrenteAdapter", "Iniciando o bind para concorrente: ${concorrente.nome}")


            if(!concorrente.foto.equals("")){
                Glide.with(itemView.context)
                    .load(concorrente.foto)
                    .into(image)
            }else{
                val defaultIcon = ContextCompat.getDrawable(itemView.context, R.drawable.icon_user)
                image.setImageDrawable(defaultIcon)
            }
            Log.d("ConcorrenteAdapter", "Imagem carregada para concorrente: ${concorrente.nome}")


            nome.text = concorrente.nome

            Log.d("nomeConcorrente","o nome do concorrente Adapter: ${concorrente.nome}")

            nome.setOnClickListener {
                onClickListener.onClick(concorrente)
            }
        }
    }
    class ItemClickListener(val clickListener:(concorrente : Concorrente) -> Unit){
        fun onClick (concorrente: Concorrente) = clickListener(concorrente)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcorrenteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_concorrentes_profissoes,parent,false)
        return ConcorrenteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaConcorrentes.size
    }

    override fun onBindViewHolder(holder: ConcorrenteViewHolder, position: Int) {
        val concorrente = listaConcorrentes[position]

        Log.d("ConcorrenteAdapter", "Posição: $position, Concorrente: ${concorrente.nome}")

        holder.bind(concorrente, onClickListener)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(concorrente)
        }

    }
}