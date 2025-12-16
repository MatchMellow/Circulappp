package com.example.CirculAPP

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Circulapp.ui.IconWithLabel

@Composable
fun MotoristaOptionsScreen(navController: NavController) {
    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ---------------------- PARTE AZUL SUPERIOR ----------------------
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
                        text = "Selecione uma das opções abaixo para iniciar rota ou informar uma parada",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = customFont
                        )
                    )
                }
            }
        }

        // ---------------------- PARTE BRANCA INFERIOR ----------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .background(Color.White)
        )
            // Botões "Iniciar Rota" e "Informar Parada"
            // Movidos para ficarem diretamente no Box, sem padding horizontal extra
            // Coluna para o botão "Sair" - mantida separada com seu próprio padding
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Esta coluna agora também preenche a largura total do Box branco
                    .padding(horizontal = 16.dp) // O padding agora é específico para esta coluna
                    .offset(y = (-80).dp), // Ajuste este offset para a posição vertical desejada
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("userSelection") },
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(0.55f) // 0.61f será da largura da coluna, que agora tem padding
                        .offset(y = (-61).dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Sair",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Sair",
                        fontFamily = customFont,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            // Dialog Sobre Nós
            if (showSobreNosDialog) {
                AlertDialog(
                    onDismissRequest = { showSobreNosDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showSobreNosDialog = false }) {
                            Text(
                                "Fechar",
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
                        }
                    },
                    title = { Text("🚌 Sobre o CirculAPP", fontSize = 24.sp) },
                    text = {
                        Text(
                            fontSize = 14.sp,
                            fontFamily = customFont,
                            text = "Este aplicativo foi desenvolvido com dedicação por Joao Matheus, estudante da Universidade Federal Rural de Pernambuco (UFRPE), com o objetivo de facilitar a mobilidade no campus e fortalecer a comunicação entre alunos e motoristas. 🎓\n\n" +
                                    "👨‍💻 Desenvolvedor\n" +
                                    "• Joao Matheus\n 📸 Instagram: @MatheusB87_\n 💻 GitHub: MatchMellow"
                        )
                    }
                )
            }

            // Dialog Contatos
            if (showContatosDialog) {
                AlertDialog(
                    onDismissRequest = { showContatosDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showContatosDialog = false }) {
                            Text(
                                "Fechar",
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
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
    val offsetY = with(density) { (screenHeight * 0.575f).roundToPx() } // 63% da altura

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.08f) // altura proporcional (7.5%)
            .offset { IntOffset(0, offsetY) }, // posição proporcional (57.5% da tela)
    ) {
        Button(
            onClick = { navController.navigate("iniciarRota") },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                "Iniciar Rota",
                fontFamily = customFont,
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(Color.White)
        )

        Button(
            onClick = { navController.navigate("informarParada") },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                "Informar Parada",
                fontFamily = customFont,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }

    }

