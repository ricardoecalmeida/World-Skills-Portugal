package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.worldskillsproject.databinding.ActivitySettingsBinding
import com.example.worldskillsproject.model.ImageUser
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy{ ActivitySettingsBinding.inflate(layoutInflater)}
    private val auth by lazy{ FirebaseAuth.getInstance()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }


    }


    fun visibilidadeButtons(){
        val currentUser = auth.currentUser

        if(currentUser == null){
            binding.buttonMessages.visibility = View.INVISIBLE
            binding.buttonBadges.visibility = View.INVISIBLE
            binding.buttonLoginPerfil.setOnClickListener{
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }else{
            binding.buttonMessages.visibility = View.VISIBLE
            binding.buttonMessages.setOnClickListener {
                startActivity(Intent(this, MessageActivity::class.java))
            }
            binding.buttonBadges.visibility = View.VISIBLE
            binding.buttonBadges.setOnClickListener{
                startActivity(Intent(this, BadgesActivity::class.java))
            }

            binding.buttonLoginPerfil.setOnClickListener{
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