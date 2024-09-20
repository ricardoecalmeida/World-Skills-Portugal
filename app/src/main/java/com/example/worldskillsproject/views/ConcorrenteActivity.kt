package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.worldskillsproject.adapter.BadgesBestAdapter
import com.example.worldskillsproject.databinding.ActivityConcorrenteBinding
import com.example.worldskillsproject.model.Badges
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ConcorrenteActivity : AppCompatActivity() {
    private val binding by lazy{ ActivityConcorrenteBinding.inflate(layoutInflater)}
    val auth by lazy { FirebaseAuth.getInstance()}
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

        binding.textBadgesEscrito.visibility = View.INVISIBLE



        val intent = intent
        val idConcorrente = intent.extras?.getString("idConcorrente")
        val idProfissao = intent.extras?.getString("idProfissao")
        val idCluster = intent.extras?.getString("idCluster")

        if(idProfissao != null && idCluster != null){
            db.collection("Cluster").document(idCluster).collection("Profissao").document(idProfissao).get()
                .addOnSuccessListener {
                    if(it.contains("Nome_profissao")){
                        val nomeProfissao = it.get("Nome_profissao").toString()
                        if(!nomeProfissao.equals("")){
                            binding.textProfissao.text = nomeProfissao
                        }
                    }else{
                        binding.textProfissaoEscrito.visibility = View.INVISIBLE
                        binding.textProfissao.visibility = View.INVISIBLE
                    }


                }
        }

        if (idConcorrente != null) {
            db.collection("Utilizador").document(idConcorrente).get()
                .addOnSuccessListener { userDocument ->
                    if (userDocument.exists()) {
                        val nome = userDocument.get("Nome").toString()
                        val foto = userDocument.get("Foto").toString()
                        if(userDocument.contains("Instituicao")) {
                            val instituicao = userDocument.get("Instituicao").toString()
                            if (!instituicao.equals("")) {
                                binding.textInstituicao.text = instituicao
                            }
                        }else {
                            binding.textInstituicaoEscrito.visibility = View.INVISIBLE
                            binding.textInstituicao.visibility = View.INVISIBLE
                        }


                        binding.textNomeUser.text = nome


                        Glide.with(this)
                            .load(foto)
                            .into(binding.imageUser)

                    }
                }
        }


        val listaBadges = ArrayList<Badges>()
        if(idConcorrente != null){
            db.collection("Utilizador").document(idConcorrente).collection("Lista_Badges")
                .orderBy("data", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnSuccessListener {
                        for (doc in it.documents) {
                            val tipo_badge = doc.get("tipo_badge").toString()
                            val id_cluster = doc.get("id_cluster").toString()
                            val id_profissao = doc.get("id_profissao").toString()
                            val data = doc.getTimestamp("data")


                            if (id_cluster != null && id_profissao != null && tipo_badge != null && data != null) {
                                db.collection("Cluster").document(id_cluster)
                                    .collection("Profissao")
                                    .document(id_profissao).get()
                                    .addOnSuccessListener { profissaoDocument ->
                                        val nome_profissao =
                                            profissaoDocument.get("Nome_profissao").toString()


                                        db.collection("Badges").document(tipo_badge).get()
                                            .addOnSuccessListener { badgeDoc ->
                                                val categoria_badge =
                                                    badgeDoc.get("Categoria").toString()

                                                listaBadges.add(
                                                    Badges(
                                                        nome_profissao,
                                                        categoria_badge,
                                                        data
                                                    )
                                                )

                                                if(listaBadges.size!=0){
                                                    binding.textBadgesEscrito.visibility = View.VISIBLE
                                                }
                                                binding.recyclerBadgesBest.layoutManager =
                                                    LinearLayoutManager(this)
                                                binding.recyclerBadgesBest.adapter =
                                                    BadgesBestAdapter(
                                                        listaBadges,
                                                        BadgesBestAdapter.ItemClickListener { badges ->
                                                            val nomeProfissao = badges.nome_profissao
                                                            val categoriaBadge = badges.categoria_badge
                                                            val dataBadge = badges.data

                                                        })
                                                }
                                            }
                                    }
                            }

                    if(listaBadges.size == 0) {
                        binding.textBadgesEscrito.visibility = View.INVISIBLE
                    }


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

        val imageUser = ImageUser(this, binding.buttonLoginPerfil)
        imageUser.carregarImagemUserLogin()

        visibilidadeButtons()
    }
}
