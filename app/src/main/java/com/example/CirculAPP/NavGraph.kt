package com.example.CirculAPP.ui

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.CirculAPP.*
import com.example.Circulapp.ui.HorariosScreen
import com.example.Circulapp.ui.InformarParadaScreen
import com.example.Circulapp.ui.Int2Screen
import com.example.Circulapp.ui.ItinerarioScreen
import com.example.CirculAPP.LoginScreen
import com.example.Circulapp.ui.UserSelectionScreen

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = "userSelection") {
    NavHost(navController = navController, startDestination = startDestination) {

        composable("userSelection") {
            UserSelectionScreen(
                onAlunoSelected = {
                    Log.d("NAV", "Aluno selecionado")
                    navController.navigate("alunoScreen")
                },
                onMotoristaSelected = {
                    Log.d("NAV", "Motorista selecionado")
                    navController.navigate("motoristaLogin")
                }
            )
        }

        composable("alunoScreen") {
            val context = LocalContext.current
            AlunoScreen(
                navController = navController,
                onAcessarMapa = {
                    Log.d("NAV", "Abrindo MapsAluno")
                    val intent = Intent(context, MapsAluno::class.java)
                    context.startActivity(intent)
                }
            )
        }

        composable("motoristaLogin") {
            LoginScreen(navController = navController)
        }

        composable("motoristaOptions") {
            MotoristaOptionsScreen(navController)
        }

        composable("iniciarRota") {
            Log.d("NAV", "Entrou em IniciarRotaScreen")
            IniciarRotaScreen(navController)
        }

        composable("informarParada") {
            InformarParadaScreen(navController)
        }

        composable("itinerario") {
            ItinerarioScreen(navController)
        }

        composable("horarios") {
            HorariosScreen(navController)
        }

        composable("aposAs19hScreen") {
            Int2Screen(navController)
        }

        composable("mapsMotorista/{horario}") { backStackEntry ->
            val context = LocalContext.current
            val horario = backStackEntry.arguments?.getString("horario") ?: "Indefinido"

            Log.d("NAV", "Navegando para MapsMotorista com horário: $horario")

            LaunchedEffect(horario) {
                if (horario != "Indefinido") {
                    val intent = Intent(context, MapsActivity::class.java).apply {
                        putExtra("rota", horario)
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("NAV", "Erro: horário não definido!")
                }
            }
        }
    }
}
