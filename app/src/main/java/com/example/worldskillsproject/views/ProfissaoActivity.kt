package com.example.worldskillsproject.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.ConcorrenteAdapter
import com.example.worldskillsproject.databinding.ActivityProfissaoBinding
import com.example.worldskillsproject.model.Concorrente
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfissaoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProfissaoBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    val listaConcorrentes = ArrayList<Concorrente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonSaberMais.visibility = View.INVISIBLE
        binding.textConcorrentes.visibility = View.INVISIBLE


        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }


        val intent = intent
        val clusterId = intent.extras?.getString("clusterId")
        val idProfissao = intent.extras?.getString("idProfissao")

        binding.buttonVerMapa.setOnClickListener {
            if (clusterId != null && idProfissao != null) {
                db.collection("Cluster").document(clusterId).collection("Profissao")
                    .document(idProfissao).get()
                    .addOnSuccessListener {
                        if(it.contains("Mapa")){
                            val mapa = it.get("Mapa").toString()
                            val intent =  Intent(this,MapaActivity::class.java)
                            intent.putExtra("mapa", mapa)
                            startActivity(intent)
                        }else{
                            startActivity(Intent(this,MapaActivity::class.java))
                        }
                    }

            }



        }





        if (clusterId != null && idProfissao != null) {
            db.collection("Cluster").document(clusterId).collection("Profissao")
                .document(idProfissao).get()
                .addOnSuccessListener { profissaoDocument ->
                    val nome = profissaoDocument.get("Nome_profissao").toString()
                    val descricao = profissaoDocument.get("Descricao").toString()
                    val link = profissaoDocument.get("Link").toString()


                    binding.textNomeProfissao.text = nome

                    binding.textDescricaoProfissao.text = descricao

                    if(link.isNotEmpty()){
                        binding.buttonSaberMais.visibility = View.VISIBLE
                        binding.buttonSaberMais.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        }

                    }
                }
        }
        if (clusterId != null && idProfissao != null) {
            db.collection("Cluster").document(clusterId).collection("Profissao")
                .document(idProfissao).collection("Concorrentes").get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        val idConcorrente = doc.id

                        db.collection("Utilizador").document(idConcorrente).get()
                            .addOnCompleteListener { doc ->

                                if(doc.isComplete) {
                                    doc.addOnSuccessListener { concorrenteDoc ->



                                        val fotoConcorrente = concorrenteDoc.get("Foto").toString()
                                        val nomeConcorrente = concorrenteDoc.get("Nome").toString()
                                        /*       Log.d("nomeConcorrente", "o nome do concorrente: ${concorrenteDoc.get("Nome")}")
                                    Log.d("fotoconcorrente", "o link da foto: ${concorrenteDoc.get("Foto")}")*/


                                        listaConcorrentes.add(
                                            Concorrente(
                                                idConcorrente,
                                                fotoConcorrente,
                                                nomeConcorrente
                                            )
                                        )

                                        if(listaConcorrentes.size!=0){
                                            binding.textConcorrentes.visibility = View.VISIBLE
                                        }

                                        binding.recyclerConcorrentesProfissao.layoutManager =
                                            LinearLayoutManager(this@ProfissaoActivity,
                                                LinearLayoutManager.HORIZONTAL,false)
                                        val tamanho = listaConcorrentes.size
                                        Log.d("tamanho lista", " ${tamanho}")

                                        binding.recyclerConcorrentesProfissao.adapter =
                                            ConcorrenteAdapter(
                                                listaConcorrentes,
                                                ConcorrenteAdapter.ItemClickListener { concorrente ->
                                                    val idConcorrente = concorrente.id
                                                    val fotoConcorrente = concorrente.foto
                                                    val nomeConcorrente = concorrente.nome

                                                    val intentConcorrente = Intent(
                                                        this@ProfissaoActivity,
                                                        ConcorrenteActivity::class.java
                                                    )
                                                    intentConcorrente.putExtra(
                                                        "idConcorrente",
                                                        idConcorrente
                                                    )
                                                    intentConcorrente.putExtra(
                                                        "nomeConcorrente",
                                                        nomeConcorrente
                                                    )
                                                    intentConcorrente.putExtra(
                                                        "idProfissao",
                                                        idProfissao
                                                    )
                                                    intentConcorrente.putExtra(
                                                        "idCluster",
                                                        clusterId
                                                    )

                                                    startActivity(intentConcorrente)
                                                })
                                    }
                                }


                            }

                    }
                }



            for (concorrente in listaConcorrentes) {
                Log.d("Concorrentessssssssssssssssssssss", "ID: ${concorrente.id}, Foto: ${concorrente.foto}, Nome: ${concorrente.nome}")
            }




        }
    }






    fun visibilidadeButtons() {
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