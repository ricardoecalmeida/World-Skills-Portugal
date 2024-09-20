package com.example.worldskillsproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskillsproject.R
import com.example.worldskillsproject.model.Cluster

class ClusterListAdapter(val listaCluster:ArrayList<Cluster>, val onClickListener: ItemClickListener) : RecyclerView.Adapter<ClusterListAdapter.ClusterViewHolder>() {


    class ClusterViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val button: Button = itemView.findViewById(R.id.button_cluster)

        fun bind(cluster: Cluster, onClickListener: ItemClickListener) {
            button.text = cluster.nome
            button.setOnClickListener {
                onClickListener.onClick(cluster)
            }
        }

    }
    class ItemClickListener(val clickListener:(cluster : Cluster) -> Unit){
        fun onClick (cluster : Cluster) = clickListener(cluster)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_cluster, parent, false)
        return ClusterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaCluster.size
    }

    override fun onBindViewHolder(holder: ClusterViewHolder, position: Int) {
        val cluster = listaCluster[position]
        holder.bind(cluster, onClickListener)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(cluster)
        }
    }
}