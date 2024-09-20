package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.worldskillsproject.adapter.BadgesAdapter
import com.example.worldskillsproject.databinding.ActivityBadgesBinding
import com.example.worldskillsproject.model.Badges
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BadgesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBadgesBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.buttonAdicionarBadges.setOnClickListener {
            startActivity(Intent(this, AddBadgesActivity::class.java))
        }

    }


    fun mostrarBadges() {
        val currentUser = auth.currentUser
        val listaBadges = ArrayList<Badges>()
        if (currentUser != null) {

            val utilizador_id = currentUser.uid

            db.collection("Utilizador").document(utilizador_id).get()
                .addOnSuccessListener { document ->
                    val nome_utilizador = document.get("Nome").toString()
                    val imagem_utilizador = document.get("Foto").toString()

                    Glide.with(this)
                        .load(imagem_utilizador)
                        .into(binding.imageUser)

                    binding.textNomeUser.text = nome_utilizador
                    Log.d(
                        "nome utilizador badges",
                        "nome utilizador badges: ${document.get("Nome")}"
                    )
                }



            db.collection("Utilizador").document(utilizador_id).collection("Lista_Badges").get()
                .addOnSuccessListener {

                    for (doc in it.documents) {
                        val tipo_badge = doc.get("tipo_badge").toString()
                        val id_cluster = doc.get("id_cluster").toString()
                        val id_profissao = doc.get("id_profissao").toString()
                        val data = doc.getTimestamp("data")

                        if (id_cluster != null && id_profissao != null && tipo_badge != null && data != null) {
                            db.collection("Cluster").document(id_cluster).collection("Profissao")
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
                                            binding.recyclerBadges.layoutManager =
                                                LinearLayoutManager(this)
                                            binding.recyclerBadges.adapter =
                                                BadgesAdapter(
                                                    listaBadges,
                                                    BadgesAdapter.ItemClickListener { badges ->
                                                        val nomeProfissao = badges.nome_profissao
                                                        val categoriaBadge = badges.categoria_badge
                                                        val dataBadge = badges.data

                                                    })

                                        }
                                }


                        }


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

            mostrarBadges()

            visibilidadeButtons()

            val imageUser = ImageUser(this, binding.buttonLoginPerfil)
            imageUser.carregarImagemUserLogin()
        }
    }
