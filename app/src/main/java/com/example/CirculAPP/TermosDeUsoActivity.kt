package com.example.CirculAPP

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration // Importe esta classe
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class TermosDeUsoActivity : AppCompatActivity() {

    // **** NOVO CÓDIGO AQUI: Trava a escala da fonte ****
    override fun attachBaseContext(newBase: Context) {
        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = 1.0f // Define a escala da fonte para 1.0f (tamanho padrão)
        val context = newBase.createConfigurationContext(newConfig)
        super.attachBaseContext(context)
    }
    // **** FIM DO NOVO CÓDIGO ****

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termos_de_uso)

        val webView: WebView = findViewById(R.id.webView)
        val btnAceitar: Button = findViewById(R.id.btnAceitar)
        val btnNaoAceitar: Button = findViewById(R.id.btnNaoAceitar)

        // Nota: A fonte do WebView é controlada pelo HTML/CSS do termos_de_uso.html
        // ou pelas configurações internas do WebView, não pelo fontScale da Activity.
        // Este travamento afetará apenas os botões e outros elementos UI nativos.
        webView.loadUrl("file:///android_asset/termos_de_uso.html")

        btnAceitar.setOnClickListener {
            Log.d("TermosDeUsoActivity", "Botão Aceitar clicado")

            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("termos_aceitos", true)
                apply()
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnNaoAceitar.setOnClickListener {
            finishAffinity()
        }
    }
}