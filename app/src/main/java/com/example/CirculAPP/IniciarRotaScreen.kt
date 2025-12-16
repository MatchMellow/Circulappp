package com.example.CirculAPP.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew // Manter se precisar em outro lugar, mas não será usado no botão Voltar
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.CirculAPP.R
import com.example.CirculAPP.customFont
import com.example.Circulapp.ui.IconWithLabel

@Composable
fun IniciarRotaScreen(navController: NavController) {
    var selectedTime by remember { mutableStateOf("") }
    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // -------------------- PARTE SUPERIOR AZUL --------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
                    .background(Color(0xFF004AAD))
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconWithLabel(
                            icon = Icons.Outlined.ContactPage,
                            label = "Sobre nós",
                            onClick = { showSobreNosDialog = true }
                        )
                        IconWithLabel(
                            icon = Icons.Outlined.Phone,
                            label = "Contatos",
                            onClick = { showContatosDialog = true }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.imagem),
                            contentDescription = "Imagem ilustrativa",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 28.dp)
                        )

                        Text(
                            text = "\nBem-vindo",
                            fontSize = 24.sp,
                            fontFamily = customFont,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Divider(
                            color = Color.White,
                            thickness = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth(0.76f)
                                .padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Selecione a rota que deseja iniciar:",
                            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = customFont),
                            color = Color.White
                        )
                    }
                }
            }

            // -------------------- PARTE INFERIOR BRANCA --------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(Color.White)
            )
        }
        // -------------------- BOTÃO VOLTAR (Agora igual ao Sair) --------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // O mesmo padding do botão Sair
                .align(Alignment.BottomCenter)
                .offset(y = (-141).dp), // Ajuste o offset para a posição final desejada
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("motoristaOptions") },
                modifier = Modifier
                    .height(45.dp) // Mesma altura
                    .fillMaxWidth(0.55f), // Mesma largura proporcional
                shape = RoundedCornerShape(30.dp), // Mesmo formato
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)) // Mesma cor
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIosNew, // Mesmo ícone
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp) // Mesmo tamanho do ícone
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Voltar",
                    color = Color.White,
                    fontSize = 16.sp, // Mesma fonte size
                    fontFamily = customFont
                )
            }


        }

        // -------------------- DIALOGS --------------------

        if (showSobreNosDialog) {
            AlertDialog(
                onDismissRequest = { showSobreNosDialog = false },
                confirmButton = {
                    TextButton(onClick = { showSobreNosDialog = false }) {
                        Text("Fechar", fontSize = 16.sp, fontFamily = customFont)
                    }
                },
                title = { Text("🚌 Sobre o CirculAPP", fontSize = 24.sp) },
                text = {
                    Text(
                        fontSize = 14.sp,
                        fontFamily = customFont,
                        text = "Este aplicativo foi desenvolvido com dedicação por Joao Matheus, estudante da Universidade Federal Rural de Pernambuco (UFRPE), com o objetivo de facilitar a mobilidade no campus e fortalecer a comunicação entre alunos e motoristas. 🎓\n\n" +
                                "👨‍💻 Desenvolvedor\n" +
                                "• Joao Matheus\n 📸 Instagram: @JoaoB87_\n 💻 GitHub: MatchMellow"
                    )

                }
            )
        }

        if (showContatosDialog) {
            AlertDialog(
                onDismissRequest = { showContatosDialog = false },
                confirmButton = {
                    TextButton(onClick = { showContatosDialog = false }) {
                        Text("Fechar", fontSize = 16.sp, fontFamily = customFont)
                    }
                },
                title = { Text("DELOGS/UFRPE", fontSize = 24.sp) },
                text = {
                    Text(
                        text = """
📍 DELOGS – Departamento de Logística e Serviços
• Diretoria: diretoria.delogs@ufrpe.br
• Resíduos laboratoriais: davv.delogs@ufrpe.br
• Segurança Universitária (DSU): seguranca.delogs@ufrpe.br
• Instagram: @ufrpedelogs

📍 Outros Setores da UFRPE
• Ouvidoria: ouvidoria@ufrpe.br
• Comunicação (Ascom): comunicacao@ufrpe.br
• Secretaria Geral: secretaria.geral@ufrpe.br
• Tecnologia da Informação (STI): sti@ufrpe.br
• Instagram oficial: @ufrpeoficial
""".trimIndent(),
                        fontSize = 14.sp,
                        fontFamily = customFont,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val offsetYHorarios = with(density) { (screenHeight * 0.575f).roundToPx() } // mesma proporção dos outros

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.08f) // mesma altura dos botões principais
            .offset { IntOffset(0, offsetYHorarios) }
    ) {
        val horarios = listOf("7h às 19h", "19h às 22h")

        horarios.forEachIndexed { index, horario ->
            Button(
                onClick = {
                    selectedTime = horario
                    Log.d("NAV", "Iniciando rota: $horario")
                    navController.navigate("mapsMotorista/$horario")
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTime == horario) Color(0xFFE3E3E3) else Color(0xFF004AAD)
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    horario,
                    color = if (selectedTime == horario) Color(0xFF004AAD) else Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = customFont)
                )
            }

            if (index == 0) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                )
            }
        }
    }
}