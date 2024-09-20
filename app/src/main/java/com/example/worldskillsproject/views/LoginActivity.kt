package com.example.worldskillsproject.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worldskillsproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityLoginBinding.inflate(layoutInflater)}
    private val auth by lazy{ FirebaseAuth.getInstance()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.buttonMenuGeral.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener{
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if(email.isEmpty() && password.isEmpty()){
                Toast.makeText(this, "Digite o email e a palavra-passe", Toast.LENGTH_SHORT).show()
            }else if(email.isEmpty()){
                Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(this,  "Digite a palavra-passe", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Login OK", Toast.LENGTH_SHORT).show()
                        finish()

                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Erro Login: ${it.message}", Toast.LENGTH_SHORT).show()
                    }

            }



        }

        binding.textRecuperar.setOnClickListener{
            startActivity(Intent(this, RecuperarPassActivity::class.java))
        }
        binding.root.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }


    }


}