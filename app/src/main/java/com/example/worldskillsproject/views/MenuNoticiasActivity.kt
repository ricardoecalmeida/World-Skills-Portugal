package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskillsproject.adapter.MenuNoticiasAdapter
import com.example.worldskillsproject.databinding.ActivityMenuNoticiasBinding
import com.example.worldskillsproject.model.ImageUser
import com.example.worldskillsproject.model.MenuNoticias
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuNoticiasActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMenuNoticiasBinding.inflate(layoutInflater)
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    lateinit var adapter: MenuNoticiasAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val menuNoticias = ArrayList<MenuNoticias>()

        db.collection("Noticia")
            .orderBy("Data", Query.Direction.DESCENDING)
            .get().addOnCompleteListener {
            for (doc in it.result.documents) {
                val imagem = doc.get("Imagem").toString()
                val titulo = doc.get("Titulo").toString()
                val data = doc.getTimestamp("Data")

                if(data!= null){
                    menuNoticias.add(MenuNoticias(imagem, titulo, data))
                }

            }
            val adapter = MenuNoticiasAdapter(menuNoticias, MenuNoticiasAdapter.OnClickListener { menuNoticias ->
                val imagem = menuNoticias.imagem
                val titulo = menuNoticias.titulo
                val data = menuNoticias.data

                val intent = Intent(this, NoticiaActivity::class.java)
                intent.putExtra("Imagem", imagem)
                intent.putExtra("Titulo", titulo)
                intent.putExtra("Data", data)
                startActivity(intent)
            })
            binding.recyclerMenuNoticias.layoutManager = LinearLayoutManager(this)
            binding.recyclerMenuNoticias.adapter = adapter
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