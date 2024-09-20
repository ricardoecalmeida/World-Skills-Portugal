package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worldskillsproject.databinding.ActivityAdicionarBadgesBinding
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AdicionarBadgesActivity : AppCompatActivity() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private val binding by lazy {
        ActivityAdicionarBadgesBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }



        binding.buttonAdicionar.setOnClickListener {
            val id_cluster = binding.editIdCluster.text.toString()
            val id_profissao = binding.editIdProfissao.text.toString()
            val tipo_badge = binding.editTipoBadge.text.toString()
            val id_badge =binding.editIdBadge.text.toString()

            val data = Timestamp.now()



            val currentUser = auth.currentUser

            val dados = mapOf(
                "id_cluster" to id_cluster,
                "id_profissao" to id_profissao,
                "tipo_badge" to tipo_badge,
                "data" to data
            )
            if (currentUser != null) {
                val id_user = currentUser.uid
                db.collection("Utilizador").document(id_user).collection("Lista_Badges").get()
                    .addOnSuccessListener { document->
                        if(document.isEmpty){
                            addBadgesUser(dados, id_user, id_badge)
                            finish()
                        } else{
                            for (doc in document.documents) {
                                val idBadges_existente = doc.id
                                if (idBadges_existente.equals(id_badge)) {
                                    Toast.makeText(this, "Já tens este Badge", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    addBadgesUser(dados,id_user,id_badge)
                                }

                            }
                            finish()
                        }

                    }
                    .addOnFailureListener {
                        addBadgesUser(dados,id_user,id_badge)
                        finish()
                    }
            }

        }

    }


    fun addBadgesUser(dados:Map<String,Any>, id_user:String, id_badge:String){
        val dbPai = db.collection("Utilizador").document(id_user)
        dbPai.collection("Lista_Badges").document(id_badge)
            .set(dados, SetOptions.merge())
            .addOnSuccessListener {
                println("Badge adiconado com sucesso.")
            }
            .addOnFailureListener { e ->
                println("erro ao adicionar o badge: $e")
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
