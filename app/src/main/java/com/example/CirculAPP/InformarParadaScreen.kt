package com.example.Circulapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.ArrowBackIosNew
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
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.CirculAPP.R
import com.example.CirculAPP.customFont
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InformarParadaScreen(navController: NavController) {
    val databaseRef = FirebaseDatabase.getInstance().getReference("motorista/localizacao")

    var selecionouParada by remember { mutableStateOf(false) }
    var tempoRestante by remember { mutableStateOf(10) }
    var selectedOption by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val offsetYParadas = with(density) { (screenHeight * 0.575f).roundToPx() } // 57.5%
    val offsetYTexto = with(density) { (screenHeight * 0.49f).roundToPx() }     // 49%
    val offsetYVoltar = with(density) { (screenHeight * 0.65f).roundToPx() }    // 65%

    fun selecionarParada(parada: String) {
        selecionouParada = true
        tempoRestante = 10

        databaseRef.setValue(
            mapOf(
                "latitude" to null,
                "longitude" to null,
                "status" to "parado",
                "parada" to parada
            )
        )

        scope.launch {
            while (tempoRestante > 0) {
                delay(1000L)
                tempoRestante--
            }
            selecionouParada = false
            selectedOption = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Parte superior azul (65%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
                    .background(Color(0xFF004AAD))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, top = 20.dp),
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
                        text = "Qual o motivo da parada?",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = customFont
                        ),
                        color = Color.White
                    )
                }
            }

            // Parte inferior branca (35%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(Color.White)
            ) {}
        }

        // Botões sobrepostos de parada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.08f)
                .offset { IntOffset(0, offsetYParadas) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val botoes = listOf(
                Triple("Abastecimento", Icons.Default.LocalGasStation, "Abastecimento"),
                Triple("Troca de turno", Icons.Default.SyncAlt, "Troca de turno"),
                Triple("Refeição", Icons.Default.Restaurant, "Refeição")
            )

            botoes.forEachIndexed { index, (label, icon, value) ->
                val isSelected = selectedOption == value
                val isDisabled = selecionouParada && !isSelected

                Button(
                    onClick = {
                        selectedOption = value
                        selecionarParada(value)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(0.dp),
                    enabled = !selecionouParada || isSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            isSelected -> Color(0xFFE3E3E3)
                            isDisabled -> Color(0xFF002B66)
                            else -> Color(0xFF004AAD)
                        }
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) Color(0xFF004AAD) else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                if (index < botoes.lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(Color.White)
                    )
                }
            }
        }

        // Texto "Aguarde"
        if (selecionouParada) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .offset { IntOffset(0, offsetYTexto) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aguarde $tempoRestante segundos para iniciar a rota ou informar outra parada...",
                    color = Color(0xFF004AAD),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = customFont
                )
            }
        }

        // Botão Voltar (agora igual ao "Sair" da MotoristaOptionsScreen)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Adiciona o mesmo padding horizontal do botão "Sair"
                .align(Alignment.BottomCenter)
                .offset(y = (-141).dp), // Ajusta este offset para a posição final desejada (inferior)
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
                    text = "Voltar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = customFont
                )
            }
        }

        // Dialogs
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
• Segurança: seguranca.delogs@ufrpe.br
• Instagram: @ufrpedelogs

📍 UFRPE
• Ouvidoria: ouvidoria@ufrpe.br
• Comunicação: comunicacao@ufrpe.br
• STI: sti@ufrpe.br
• Instagram: @ufrpeoficial
""".trimIndent(),
                        fontSize = 14.sp,
                        fontFamily = customFont,
                        color = Color.Black
                    )
                }
            )
        }
    }
}
