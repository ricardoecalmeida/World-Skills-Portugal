package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.ProfissoesListAdapter
import com.example.worldskillsproject.databinding.ActivityProfissoesBinding
import com.example.worldskillsproject.model.ImageUser
import com.example.worldskillsproject.model.Profissao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfissoesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProfissoesBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
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
        mostarProfissoes()


    }

    fun mostarProfissoes() {

        val intent = intent
        val clusterId = intent.extras?.getString("clusterId")
        val clusterNome = intent.extras?.getString("clusterNome")

        binding.textNomeCluster.text = clusterNome


        /*  Log.d("ProfissoesActivity","ID do cluster: $clusterId")*/

        val listaProfissoes = ArrayList<Profissao>()

        if (clusterId != null) {
            db.collection("Cluster").document(clusterId).collection("Profissao").get()
                .addOnSuccessListener {

                    for (doc in it.documents) {
                        /*Log.d("IDCluster","ID do cluster Profissao: ${doc.id}")*/
                        val nome = doc.get("Nome_profissao").toString()
                        /* Log.d("IDCluster","ID do cluster Profissao: ${doc.get("Profissao")}")*/
                        val idProfissao = doc.id
                        val descricao = doc.get("Descricao").toString()
                        val link = doc.get("Link").toString()
                        listaProfissoes.add(Profissao(idProfissao, nome, descricao, link))



                        binding.recyclerProfissoes.layoutManager = LinearLayoutManager(this)
                        binding.recyclerProfissoes.adapter =
                            ProfissoesListAdapter(
                                listaProfissoes,
                                ProfissoesListAdapter.ItemClickListener { profissao ->

                                    val idProfissao = profissao.id
                                    val intentProfissao =
                                        Intent(
                                            this@ProfissoesActivity,
                                            ProfissaoActivity::class.java
                                        )
                                    intentProfissao.putExtra("idProfissao", idProfissao)
                                    intentProfissao.putExtra("clusterId", clusterId)

                                    startActivity(intentProfissao)

                                })
                    }

                }
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