package com.example.CirculAPP

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.CirculAPP.ui.NavGraph
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instala a SplashScreen
        val splashScreen: SplashScreen = installSplashScreen()

        // Variável de controle para manter a splash visível
        var isLoading = true

        // Mantém a splash enquanto isLoading for true
        splashScreen.setKeepOnScreenCondition { isLoading }

        // Simula um atraso de 1 segundo (executa em outra thread)
        Thread {
            Thread.sleep(2000)
            isLoading = false
        }.start()

        super.onCreate(savedInstanceState)

        // Define o tema principal após a splash
        setTheme(R.style.Theme_App)

        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val termosAceitos = sharedPref.getBoolean("termos_aceitos", false)

        if (!termosAceitos) {
            // Redireciona para os Termos de Uso
            startActivity(Intent(this, TermosDeUsoActivity::class.java))
            finish()
        } else {
            // Carrega o conteúdo principal do app com fonte travada
            setContent {
                val navController = rememberNavController()
                val startDestination = intent.getStringExtra("startDestination") ?: "userSelection"

                // Aqui travamos o tamanho da fonte
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = LocalDensity.current.density,
                        fontScale = 1f // trava a escala da fonte, mesmo se o usuário tiver aumentado nas configs
                    )
                ) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        NavGraph(navController = navController, startDestination = startDestination)
                    }
                }
            }
        }
    }
}
