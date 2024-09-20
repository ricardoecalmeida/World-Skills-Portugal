package com.example.worldskillsproject.model

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ImageUser (private val activity: Context, private val localImagem : ImageView) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun carregarImagemUserLogin(){
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val utilizador_id = currentUser.uid

            db.collection("Utilizador").document(utilizador_id).get()
                .addOnSuccessListener { document ->
                    val imagem_utilizador = document.get("Foto").toString()

                    if(!imagem_utilizador.equals("")){
                        Glide.with(activity)
                            .load(imagem_utilizador)
                            .into(localImagem)
                    }

                }



        }
    }


}