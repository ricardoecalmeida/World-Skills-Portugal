package com.example.worldskillsproject.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Badges
import java.util.Locale

class BadgesBestAdapter(val listaBadges:ArrayList<Badges>, val onClickListener: ItemClickListener) : RecyclerView.Adapter<BadgesBestAdapter.BadgeViewHolder>() {



    class BadgeViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val text_badge_nome_tipo_badge : TextView = ItemView.findViewById((R.id.text_badge_nome_tipo_badge))
        val text_badge_nome_profissao : TextView = ItemView.findViewById((R.id.text_badge_nome_profissao))
        val text_badge_data : TextView = ItemView.findViewById((R.id.text_badge_data))



        fun bind(badges: Badges, onClickListener: ItemClickListener) {
            text_badge_nome_tipo_badge.text = badges.categoria_badge
            text_badge_nome_profissao.text = badges.nome_profissao

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dataString = dateFormat.format(badges.data.toDate())
            text_badge_data.text = dataString


        }

    }
    class ItemClickListener(val clickListener:(badges : Badges) -> Unit){
        fun onClick (badges : Badges) = clickListener(badges)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_badges_best, parent, false)
        return BadgeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaBadges.size
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badges = listaBadges[position]
        holder.bind(badges, onClickListener)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(badges)
        }
    }
}