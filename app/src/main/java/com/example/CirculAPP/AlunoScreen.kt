package com.example.CirculAPP

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Circulapp.ui.IconWithLabel


val customFont = FontFamily(Font(R.font.sam_font1))

@Composable
fun AlunoScreen(
    navController: NavController,
    onAcessarMapa: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("Aluno") }

    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Parte Azul (65%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
                    .background(Color(0xFF004AAD))
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp),

                ) {
                // Ícones Sobre nós e Contatos no topo com controle de distâncias
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, top = 12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconWithLabel(
                        icon = Icons.Outlined.ContactPage,
                        label = "Sobre nós",
                        onClick = { showSobreNosDialog = true }
                    )
                    Spacer(modifier = Modifier.width(32.dp)) // 👈 distância horizontal entre os ícones
                    IconWithLabel(
                        icon = Icons.Outlined.Phone,
                        label = "Contatos",
                        onClick = { showContatosDialog = true }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),


                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                )

                {

                    Image(
                        painter = painterResource(id = R.drawable.imagem),
                        contentDescription = "Imagem ilustrativa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
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

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.76f)
                            .padding(vertical = 16.dp), thickness = 2.dp, color = Color.White
                    )

                    Text(
                        text = "Clique em \"Acessar\" para acompanhar o circular em tempo real",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = customFont
                    )
                }
            }

            // Parte Branca (35%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-80).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { onAcessarMapa() },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(0.55f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF004AAD),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Icon(
                            Icons.Filled.KeyboardDoubleArrowUp,
                            contentDescription = "Seta cima",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Acessar", fontSize = 16.sp, fontFamily = customFont)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.KeyboardDoubleArrowUp,
                            contentDescription = "Seta cima",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Linha com Itinerário e Horários
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate("itinerario") },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp)
                                .padding(end = 4.dp)
                                .offset(y = 61.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
                        ) {
                            Icon(Icons.Outlined.Map, contentDescription = "Itinerário", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Itinerário", color = Color.White, fontSize = 16.sp, fontFamily = customFont)
                        }

                        Button(
                            onClick = { navController.navigate("horarios") },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp)
                                .padding(start = 4.dp)
                                .offset(y = 61.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
                        ) {
                            Icon(Icons.Outlined.Schedule, contentDescription = "Horários", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Horários", color = Color.White, fontSize = 16.sp, fontFamily = customFont)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("userSelection") },
                        modifier = Modifier
                            .height(45.dp)
                            .offset(y = (-61).dp)
                            .fillMaxWidth(0.55f),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
                    ) {
                        Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Voltar", tint = Color.White,                                 modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Voltar", color = Color.White, fontSize = 16.sp, fontFamily = customFont)
                    }
                }
            }
        }

        // Botões "Aluno | Motorista" sobrepostos com altura e posição proporcional
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val density = LocalDensity.current
        val offsetY = with(density) { (screenHeight * 0.575f).roundToPx() } // 63% da altura

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.08f) // altura proporcional (7.5%)
                    .offset { IntOffset(0, offsetY) } // posição proporcional
            ) {
                Button(
                    onClick = {
                        selectedOption = "Aluno"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "Aluno") Color(0xFFE3E3E3) else Color(
                            0xFF004AAD
                        )
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        "Aluno",
                        fontSize = 16.sp,
                        color = if (selectedOption == "Aluno") Color(0xFF004AAD) else Color.White,                    fontFamily = customFont,

                        )
                }

                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                )

                Button(
                    onClick = {
                        selectedOption = "Motorista"
                        navController.navigate("motoristaLogin")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "Motorista") Color(0xFFE3E3E3) else Color(
                            0xFF004AAD
                        )
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        "Motorista",
                        fontSize = 16.sp,
                        color = if (selectedOption == "Motorista") Color(0xFF004AAD) else Color.White,                    fontFamily = customFont,

                        )
                }
            }
        }// Dialog Sobre Nós
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
    }




