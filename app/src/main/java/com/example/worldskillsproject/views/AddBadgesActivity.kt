package com.example.worldskillsproject.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worldskillsproject.databinding.ActivityAddBadgesBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class AddBadgesActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private val binding by lazy {
        ActivityAddBadgesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonScan.setOnClickListener {
            startQRScanner()
        }


    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false) // Desabilitar som ao detectar um código QR
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result: IntentResult? =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            result?.let {
                if (it.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    val qrContent = it.contents
                    val qrVariables =
                        qrContent.split(";")
                    if (qrVariables.size == 4) {
                        val id_cluster = qrVariables[0]
                        val id_profissao = qrVariables[1]
                        val tipo_badge = qrVariables[2]
                        val id_badge = qrVariables[3]

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
}