package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.RecomendacoesAdapter
import com.example.worldskillsproject.databinding.ActivityRecomendacoesBinding
import com.example.worldskillsproject.model.ImageUser
import com.example.worldskillsproject.model.Recomendacoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecomendacoesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRecomendacoesBinding.inflate(layoutInflater)
    }

    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    lateinit var adapter : RecomendacoesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val recomendacoes = ArrayList<Recomendacoes>()

        db.collection("Recomendacao").get().addOnCompleteListener {
            for (doc in it.result.documents) {
                val descricao = doc.get("Descricao").toString()
                recomendacoes.add(Recomendacoes(descricao))
            }

            val adapter = RecomendacoesAdapter(recomendacoes, RecomendacoesAdapter.OnClickListener { recomendacoes ->

            })
            binding.recyclerRecomendacoes.layoutManager = LinearLayoutManager(this)
            binding.recyclerRecomendacoes.adapter = adapter
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