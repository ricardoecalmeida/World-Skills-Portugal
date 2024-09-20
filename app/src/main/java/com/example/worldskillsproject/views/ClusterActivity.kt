package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.ClusterListAdapter
import com.example.worldskillsproject.databinding.ActivityClusterBinding
import com.example.worldskillsproject.model.Cluster
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClusterActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val binding by lazy { ActivityClusterBinding.inflate(layoutInflater) }
    private val db by lazy { FirebaseFirestore.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        mostrarCluster()

    }

    fun mostrarCluster(){
        val listaCluster = ArrayList<Cluster>()

        db.collection("Cluster").get()
            .addOnSuccessListener {
                for (doc in it.documents) {
                    val nome = doc.get("Nome").toString()
                    val id = doc.id
                    listaCluster.add(Cluster(id, nome))

                }

                binding.recyclerCluster.layoutManager = LinearLayoutManager(this@ClusterActivity)
                binding.recyclerCluster.adapter =
                    ClusterListAdapter(listaCluster,ClusterListAdapter.ItemClickListener {cluster ->

                        val clusterId = cluster.id
                        val clusterNome = cluster.nome
                        /*  Log.d("ClusterActivity","ID do cluster: $clusterId")*/
                        val intent = Intent(this@ClusterActivity, ProfissoesActivity::class.java)
                        intent.putExtra("clusterId", clusterId)
                        intent.putExtra("clusterNome", clusterNome)
                        startActivity(intent)


                    })

            }
    }
    fun visibilidadeButtons(){
        val currentUser = auth.currentUser

        if (currentUser == null) {
            binding.buttonMessages.visibility = View.INVISIBLE
            binding.buttonBadges.visibility = View.INVISIBLE
            binding.buttonLoginPerfil.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        } else {
            binding.buttonMessages.visibility = View.VISIBLE
            binding.buttonMessages.setOnClickListener {
                startActivity(Intent(this, MessageActivity::class.java))
            }
            binding.buttonBadges.visibility = View.VISIBLE
            binding.buttonBadges.setOnClickListener {
                startActivity(Intent(this, BadgesActivity::class.java))
            }

            binding.buttonLoginPerfil.setOnClickListener {
                startActivity(Intent(this, UtilizadorActivity::class.java))
            }

        }
    }

    override fun onResume() {
        super.onResume()

        visibilidadeButtons()

        val imageUser = ImageUser(this, binding.buttonLoginPerfil)
        imageUser.carregarImagemUserLogin()

    }
}