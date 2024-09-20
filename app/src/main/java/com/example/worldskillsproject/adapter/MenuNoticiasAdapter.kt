package com.example.worldskillsproject.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.MenuNoticias
import java.util.Locale

class MenuNoticiasAdapter (
    val menuNoticias : ArrayList<MenuNoticias>,
    val onClickListener : OnClickListener
) : RecyclerView.Adapter<MenuNoticiasAdapter.menuNoticiasViewHolder>() {
    class menuNoticiasViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image_noticia : ImageView = ItemView.findViewById(R.id.image_noticias)
        val text_titulo : TextView = ItemView.findViewById(R.id.text_noticia_titulo)
        val text_data : TextView = ItemView.findViewById(R.id.text_noticia_data)


    }
    class OnClickListener (val clickListener: (menuNoticias : MenuNoticias) -> Unit) {
        fun onClick(menuNoticias: MenuNoticias) = clickListener(menuNoticias)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuNoticiasViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_menu_noticias, parent, false)
        return menuNoticiasViewHolder(view)
    }
    override fun getItemCount(): Int {
        return menuNoticias.size
    }

    override fun onBindViewHolder(holder: menuNoticiasViewHolder, position: Int) {
        val menuNoticias = menuNoticias[position]
        holder.text_titulo.text = menuNoticias.titulo


        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataString = dateFormat.format(menuNoticias.data.toDate())
        holder.text_data.text = dataString

        Glide.with(holder.itemView.context)
            .load(menuNoticias.imagem)
            .into(holder.image_noticia)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(menuNoticias)
        }

    }

}