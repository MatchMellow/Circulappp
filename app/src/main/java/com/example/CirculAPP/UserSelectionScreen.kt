package com.example.Circulapp.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.CirculAPP.R
import com.example.CirculAPP.customFont

@Composable
fun IconWithLabel(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontFamily = customFont,
        )
    }
}

@Composable
fun UserSelectionScreen(
    onAlunoSelected: () -> Unit,
    onMotoristaSelected: () -> Unit
) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") }

    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val offsetY = with(density) { (screenHeight * 0.65f).roundToPx() } // 65% da altura

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Parte superior azul (65%)
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .background(Color(0xFF004AAD))
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
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
                        Spacer(modifier = Modifier.width(32.dp))
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
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.imagem),
                            contentDescription = "Imagem de exemplo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        )
                        Text(
                            text = "\nOlá, você é aluno ou motorista?",
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
                            text = "Selecione abaixo uma das opções",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = customFont,
                        )
                    }
                }
            }

            // Parte inferior branca (35%)
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .background(Color.White)
            ) {
                // Botão "Sair"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-80).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (context is Activity) {
                                context.finish()
                            }
                        },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(0.55f)
                            .offset(y = (-61).dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Sair",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = customFont,
                        )
                    }
                }
            }
        }

        // Botões sobrepostos com altura e posição proporcional
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
                    .offset { IntOffset(0, offsetY) }, // posição proporcional (57.5% da tela)
            ) {
                Button(
                    onClick = {
                        selectedOption = "Aluno"
                        onAlunoSelected()
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
                        color = if (selectedOption == "Aluno") Color(0xFF004AAD) else Color.White,
                        fontSize = 16.sp,
                        fontFamily = customFont,
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
                        onMotoristaSelected()
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
                        color = if (selectedOption == "Motorista") Color(0xFF004AAD) else Color.White,
                        fontSize = 16.sp,
                        fontFamily = customFont,
                    )
                }
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
}
