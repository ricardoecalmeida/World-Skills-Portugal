package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.worldskillsproject.adapter.NoticiaAdapter
import com.example.worldskillsproject.databinding.ActivityNoticiaBinding
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoticiaActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNoticiaBinding.inflate(layoutInflater)
    }
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    lateinit var adapter: NoticiaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val titulo = intent.extras?.getString("Titulo")
        val data = intent.extras?.getString("Data")
        val imagem = intent.extras?.getString("Imagem")


        binding.textNoticiaTitulo.text = titulo
        binding.textNoticiaData.text = data

        if (titulo != null) {
            db.collection("Noticia")
                .whereEqualTo("Titulo", titulo)
                .get()
                .addOnCompleteListener { titulo ->
                    if (titulo.isSuccessful) {
                        for (doc in titulo.result.documents) {
                            val conteudo = doc.get("Conteudo").toString()
                            Glide.with(this)
                                .load(imagem)
                                .into(binding.imagemNoticia)
                            binding.textNoticiaConteudo.text = conteudo
                        }
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