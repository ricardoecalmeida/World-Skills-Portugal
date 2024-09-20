package com.example.worldskillsproject.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worldskillsproject.databinding.ActivityRecuperarPassBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPassActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityRecuperarPassBinding.inflate(layoutInflater)}
    private val auth by lazy{ FirebaseAuth.getInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonRecuperar.setOnClickListener{
            val email = binding.editEmail.text.toString()



            if(email.isEmpty()){
                Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show()
            }else{
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Verifique o seu email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }

                    .addOnFailureListener{
                        Toast.makeText(this, "Erro ao reuperar a palavra-passe", Toast.LENGTH_SHORT).show()
                    }

            }


        }

        binding.buttonBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        binding.buttonMenuGeral.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }
}