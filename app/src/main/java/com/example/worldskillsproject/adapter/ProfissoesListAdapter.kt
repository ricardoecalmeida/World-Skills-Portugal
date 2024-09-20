package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Profissao

class ProfissoesListAdapter(val listaProfissoes:ArrayList<Profissao>, val onClickListener: ItemClickListener) : RecyclerView.Adapter<ProfissoesListAdapter.ProfissaoViewHolder>() {


    class ProfissaoViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val button: Button = itemView.findViewById(R.id.button_profissoes)

        fun bind(profissao: Profissao, onClickListener: ProfissoesListAdapter.ItemClickListener) {
            button.text = profissao.nome
            button.setOnClickListener {
                onClickListener.onClick(profissao)
            }

        }
    }
    class ItemClickListener(val clickListener:(profissao : Profissao) -> Unit){
        fun onClick (profissao : Profissao) = clickListener(profissao)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfissaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_profissoes_list,parent,false)
        return ProfissaoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaProfissoes.size
    }

    override fun onBindViewHolder(holder: ProfissaoViewHolder, position: Int) {
        val profissao = listaProfissoes[position]
        holder.bind(profissao, onClickListener)

        holder.itemView.setOnClickListener{
            onClickListener.onClick(profissao)
        }
    }
}