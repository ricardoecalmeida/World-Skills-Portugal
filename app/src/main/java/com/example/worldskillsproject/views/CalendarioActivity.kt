package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.CalendarioAdapter
import com.example.worldskillsproject.databinding.ActivityCalendarioBinding
import com.example.worldskillsproject.databinding.ActivityContactosBinding
import com.example.worldskillsproject.model.Calendario
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalendarioActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCalendarioBinding.inflate(layoutInflater)
    }
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    lateinit var adapter : CalendarioAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener{startActivity(Intent(this, HomeActivity::class.java))}
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val calendario = ArrayList<Calendario>()

        db.collection("Calendario").get().addOnCompleteListener {
            for (doc in it.result.documents) {
                val data = doc.get("Data").toString()
                val evento = doc.get("Evento").toString()
                calendario.add(Calendario(data, evento))
            }

            val adapter = CalendarioAdapter(calendario, CalendarioAdapter.OnClickListener { calendario ->
            })
            binding.recyclerCalendario.layoutManager = LinearLayoutManager(this)
            binding.recyclerCalendario.adapter = adapter
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