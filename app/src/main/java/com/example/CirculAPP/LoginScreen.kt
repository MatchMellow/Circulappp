package com.example.CirculAPP

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Login
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Circulapp.ui.IconWithLabel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var lembrarSenha by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Motorista") }

    val database = FirebaseDatabase.getInstance()
    val motoristasRef = database.reference.child("motorista")

    var showSobreNosDialog by remember { mutableStateOf(false) }
    var showContatosDialog by remember { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val offsetY = with(density) { (screenHeight * 0.575f).roundToPx() } // 57.5% da altura da tela

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
                    Spacer(modifier = Modifier.width(32.dp)) // distância horizontal entre ícones
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
                        contentDescription = "Imagem ilustrativa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    )
                    Text(
                        text = "\nLogin do motorista",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = customFont,
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
                        text = "Bem vindo, $username",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        fontFamily = customFont,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Parte branca (35%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(0.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { input ->
                            if (input.matches(Regex("^[a-zA-Z0-9]*$"))) {
                                username = input
                            }
                        },
                        label = {
                            Text(
                                "Usuário",
                                color = Color(0xFF004AAD),
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF004AAD),
                            unfocusedBorderColor = Color(0xFF004AAD),
                            cursorColor = Color(0xFF004AAD)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Senha",
                                color = Color(0xFF004AAD),
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF004AAD),
                            unfocusedBorderColor = Color(0xFF004AAD),
                            cursorColor = Color(0xFF004AAD)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = lembrarSenha,
                            onCheckedChange = { lembrarSenha = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF004AAD),
                                uncheckedColor = Color(0xFF004AAD),
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            text = "Lembrar-me",
                            color = Color(0xFF004AAD),
                            fontSize = 16.sp,
                            fontFamily = customFont
                        )
                    }

                    if (loginError) {
                        Text(
                            text = "Usuário ou senha incorretos",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            fontFamily = customFont
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = { navController.navigate("userSelection") },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF004AAD)
                            )
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
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
                        }

                        Button(
                            onClick = {
                                val userRef = motoristasRef.child(username)
                                userRef.get().addOnSuccessListener { snapshot ->
                                    val senhaArmazenada = snapshot.child("senha").getValue<String>()
                                    if (senhaArmazenada == password) {
                                        navController.navigate("motoristaOptions")
                                    } else {
                                        loginError = true
                                    }
                                }.addOnFailureListener {
                                    loginError = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF004AAD)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Login,
                                contentDescription = "Entrar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Entrar",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = customFont
                            )
                        }
                    }
                }
            }
        }

        // Botões "Aluno | Motorista" sobrepostos fora da coluna, para ficar sobre a transição azul/branco
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.08f) // 8% altura
                .offset { IntOffset(0, offsetY) } // 57.5% altura da tela
                .align(Alignment.TopStart)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        selectedOption = "Aluno"
                        navController.navigate("AlunoScreen")
                    },
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
                        color = if (selectedOption == "Aluno") Color(0xFF004AAD) else Color.White,
                        fontSize = 16.sp,
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
                        color = if (selectedOption == "Motorista") Color(0xFF004AAD) else Color.White,
                        fontSize = 16.sp,
                        fontFamily = customFont
                    )
                }
            }
        }

        // Dialogs ...
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
                                "• Joao Matheus\n 📸 Instagram: @MatchMellow_\n 💻 GitHub: MatchMellow"
                    )

                }
            )
        }

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
