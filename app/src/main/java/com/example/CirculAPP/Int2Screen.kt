package com.example.Circulapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.CirculAPP.R
import com.example.CirculAPP.customFont

@Composable
fun Int2Screen(navController: NavHostController) {
    var selectedOption by remember { mutableStateOf("Aluno") }
    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val offsetY = with(density) { (screenHeight * 0.575f).roundToPx() } // 57.5%
    val whiteAreaHeight = screenHeight * 0.35f
    val sairOffset = with(density) { (whiteAreaHeight * 0.08f).roundToPx() } // 8% da parte branca

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
                    .background(Color(0xFF004AAD))
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp),

            ) {
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
                    Spacer(modifier = Modifier.width(32.dp))
                    IconWithLabel(
                        icon = Icons.Outlined.Phone,
                        label = "Contatos",
                        onClick = { showContatosDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Image(
                    painter = painterResource(id = R.drawable.itinerariopos19),
                    contentDescription = "Mapa do itinerário",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Text(
                    text = "\nMapa do itinerário",
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
                    text = "Horário:\n🕒 19:00 - 22:00",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = customFont
                    ),
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {}

        }

        // Botões "Aluno | Motorista" sobrepostos
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.08f)
                .offset { IntOffset(0, offsetY) }
                .align(Alignment.TopStart)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { selectedOption = "Aluno" },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "Aluno") Color(0xFFE3E3E3) else Color(0xFF004AAD)
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        "Aluno",
                        fontSize = 16.sp,
                        color = if (selectedOption == "Aluno") Color(0xFF004AAD) else Color.White,
                        fontFamily = customFont
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
                        containerColor = if (selectedOption == "Motorista") Color(0xFFE3E3E3) else Color(0xFF004AAD)
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        "Motorista",
                        fontSize = 16.sp,
                        color = if (selectedOption == "Motorista") Color(0xFF004AAD) else Color.White,
                        fontFamily = customFont
                    )
                }
            }
        }

        // Botão "Voltar" centralizado e proporcional
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-80).dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(45.dp)
                    .offset(y = (-61).dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD),
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Voltar",
                        fontSize = 16.sp,
                        fontFamily = customFont
                    )
                }
            }
        }

        // Diálogos
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
                        color = Color.Black
                    )
                }
            )
        }

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
                        text = "Este aplicativo foi desenvolvido com dedicação por João Matheus, estudante da Universidade Federal Rural de Pernambuco (UFRPE), com o objetivo de facilitar a mobilidade no campus e fortalecer a comunicação entre alunos e motoristas. 🎓\n\n" +
                                "👨‍💻 Desenvolvedor\n" +
                                "• JOÃO MATHEUS  💻 GitHub: MatchMellow"
                    )
                }
            )
        }
    }
}
