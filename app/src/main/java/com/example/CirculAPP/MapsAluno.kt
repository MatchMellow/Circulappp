    package com.example.CirculAPP

    import android.Manifest
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.content.pm.PackageManager
    import android.content.res.Configuration // Importe esta classe
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.os.Build
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.util.Log
    import android.widget.Button
    import android.widget.CheckBox
    import android.widget.LinearLayout
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.app.ActivityCompat
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import com.example.CirculAPP.databinding.ActivityMapsBinding
    import com.google.android.gms.maps.CameraUpdateFactory
    import com.google.android.gms.maps.GoogleMap
    import com.google.android.gms.maps.OnMapReadyCallback
    import com.google.android.gms.maps.SupportMapFragment
    import com.google.android.gms.maps.model.*
    import com.google.firebase.database.*
    import java.util.Calendar

    class MapsAluno : AppCompatActivity(), OnMapReadyCallback {

        private lateinit var mMap: GoogleMap
        private lateinit var binding: ActivityMapsBinding
        private val database = FirebaseDatabase.getInstance()
        private val motoristaRef = database.getReference("motorista/localizacao")
        private var motoristaMarker: Marker? = null
        private var motoristaLocation: LatLng? = null
        private var isPositionLocked = false
        private var currentZoomLevel = 16f
        private val handler = Handler(Looper.getMainLooper())
        private val updateInterval: Long = 30000

        private var notificacaoAtivada = false

        // Controle separado das notificações:
        private var notificacaoMostradaInicioRota = false
        // Agora um set pra rastrear paradas notificadas
        private val paradasNotificadas = mutableSetOf<String>()

        private val CHANNEL_ID = "circulapp_notifications"
        private val NOTIFICATION_ID = 1

        private val prefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

        private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permissão de notificação concedida!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissão negada. Notificações não funcionarão.", Toast.LENGTH_SHORT).show()
            }
        }

        // **** NOVO CÓDIGO AQUI: Trava a escala da fonte ****
        override fun attachBaseContext(newBase: Context) {
            // Obtém a configuração atual do contexto
            val newConfig = Configuration(newBase.resources.configuration)
            // Define a escala da fonte para 1.0f (tamanho padrão do sistema)
            newConfig.fontScale = 1.0f
            // Cria um novo contexto com a configuração modificada
            val context = newBase.createConfigurationContext(newConfig)
            // Chama o método da superclasse com o novo contexto
            super.attachBaseContext(context)
        }
        // **** FIM DO NOVO CÓDIGO ****

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            checkNotificationPermission()
            createNotificationChannel()

            binding = ActivityMapsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            notificacaoAtivada = prefs.getBoolean("notificacaoAtivada", false)

            val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
            currentZoomLevel = sharedPreferences.getFloat("zoomLevel", 16f)

            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)

            val checkBoxNotification = findViewById<CheckBox>(R.id.checkBoxNotification)
            checkBoxNotification.isChecked = notificacaoAtivada
            checkBoxNotification.setOnCheckedChangeListener { _, isChecked ->
                notificacaoAtivada = isChecked
                notificacaoMostradaInicioRota = false
                paradasNotificadas.clear()
                prefs.edit().putBoolean("notificacaoAtivada", isChecked).apply()
                Toast.makeText(this, if (isChecked) "Notificações ativadas" else "Notificações desativadas", Toast.LENGTH_SHORT).show()
            }

            motoristaRef.keepSynced(true)
            listenMotoristaLocation()

            findViewById<Button>(R.id.btnVoltar).setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            findViewById<Button>(R.id.btnCentralizarCircular).setOnClickListener {
                motoristaLocation?.let {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, currentZoomLevel))
                }
            }

            findViewById<Button>(R.id.btnTravarPosicao).setOnClickListener {
                isPositionLocked = !isPositionLocked
                Toast.makeText(this, if (isPositionLocked) "Posição Destravada" else "Posição Travada", Toast.LENGTH_SHORT).show()
            }

            startUpdating()
        }

        private fun checkNotificationPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when {
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                        // Permissão já concedida
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                        Toast.makeText(this, "Precisamos da permissão para enviar notificações.", Toast.LENGTH_LONG).show()
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Notificações Circular"
                val descriptionText = "Canal para notificações do app Circular"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500) // 3 vibrações
                    setSound(null, null) // Sem som, só vibração
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        private fun startUpdating() {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    listenMotoristaLocation()
                    handler.postDelayed(this, updateInterval)
                }
            }, updateInterval)
        }

        private fun listenMotoristaLocation() {
            motoristaRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val now = Calendar.getInstance()
                    val hora = now.get(Calendar.HOUR_OF_DAY)
                    val minuto = now.get(Calendar.MINUTE)
                    val totalMinutos = hora * 60 + minuto

                    val limiteInicio = 6 * 60 + 40
                    val limiteFim = 22 * 60 + 20

                    if (totalMinutos in limiteInicio..limiteFim) {
                        val latitude = snapshot.child("latitude").getValue(Double::class.java)
                        val longitude = snapshot.child("longitude").getValue(Double::class.java)
                        val status = snapshot.child("status").getValue(String::class.java) ?: "Indisponível"
                        val parada = snapshot.child("parada").getValue(String::class.java)?.trim() ?: "Parada não definida"
                        val rota = snapshot.child("rota").getValue(String::class.java) ?: "Não definida"

                        val mensagemTexto = findViewById<TextView>(R.id.mensagemTexto)
                        val mensagemContainer = findViewById<LinearLayout>(R.id.mensagemContainer)

                        runOnUiThread {
                            mensagemContainer.visibility = LinearLayout.VISIBLE
                        }

                        runOnUiThread {
                            // Notificação início de rota
                            if (status == "em rota" && notificacaoAtivada && !notificacaoMostradaInicioRota) {
                                showExternalNotification("🚍 O Circular iniciou a rota!")
                                notificacaoMostradaInicioRota = true
                            } else if (status != "em rota") {
                                notificacaoMostradaInicioRota = false
                            }

                            // Notificação para paradas específicas - verifica se já notificou essa parada
                            if (status == "parado" && notificacaoAtivada &&
                                (parada == "Abastecimento" || parada == "Troca de Turno" || parada == "Refeição")) {

                                if (!paradasNotificadas.contains(parada)) {
                                    val textoNotificacao = when (parada) {
                                        "Abastecimento" -> "🛑 Circular parou para abastecer ⛽"
                                        "Troca de Turno" -> "🛑 Circular parou para troca de turno 🔄"
                                        "Refeição" -> "🛑 Motorista fez uma pausa para refeição 🍽️"
                                        else -> null
                                    }
                                    textoNotificacao?.let {
                                        showExternalNotification(it)
                                        paradasNotificadas.add(parada)
                                    }
                                }
                            } else if (status != "parado") {
                                paradasNotificadas.clear()
                            }

                            if (status == "indisponível" || status == "inativa") {
                                motoristaMarker?.remove()
                                motoristaMarker = null
                                mensagemTexto.text = "❌ Circular não emitiu dados de localização ❌"
                                mensagemTexto.textSize = 14f
                            } else if (status == "parado") {
                                mensagemTexto.text = when (parada) {
                                    "Abastecimento" -> "🛑 Circular parou para abastecer ⛽"
                                    "Troca de Turno" -> "🛑 Circular parou para troca de turno 🔄"
                                    "Refeição" -> "🛑 Motorista fez uma pausa para refeição 🍽️"
                                    "Outro" -> "🛑 Circular parou por motivo desconhecido"
                                    else -> "❌ Circular não emitiu dados de localização ❌"
                                }
                                mensagemTexto.textSize = 14f
                            } else if (status == "em rota") {
                                if (latitude != null && longitude != null) {
                                    motoristaLocation = LatLng(latitude, longitude)

                                    motoristaMarker?.remove()
                                    motoristaMarker = mMap.addMarker(
                                        MarkerOptions()
                                            .position(motoristaLocation!!)
                                            .title("Circular Está Aqui")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_motorista))
                                    )

                                    if (!isPositionLocked) {
                                        mMap.animateCamera(CameraUpdateFactory.newLatLng(motoristaLocation!!))
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(motoristaLocation!!, currentZoomLevel))
                                    }

                                    mensagemTexto.text = "🚍 Circular está em rota: $rota"
                                    mensagemTexto.textSize = 14f
                                } else {
                                    mensagemTexto.text = "🚨 Erro: Não foi possível obter a localização do circular. Tente novamente."
                                    mensagemTexto.textSize = 14f
                                    Toast.makeText(this@MapsAluno, "Erro: Localização não encontrada. Tente novamente.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                mensagemTexto.text = "Localização indisponível"
                                mensagemTexto.textSize = 14f
                            }
                        }
                    } else {
                        runOnUiThread {
                            val mensagemTexto = findViewById<TextView>(R.id.mensagemTexto)
                            val mensagemContainer = findViewById<LinearLayout>(R.id.mensagemContainer)

                            mensagemContainer.visibility = LinearLayout.VISIBLE
                            mensagemTexto.text = "🕒 Circular fora do horário de funcionamento"
                            mensagemTexto.textSize = 14f
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Erro ao acessar Firebase: ${error.message}")
                    runOnUiThread {
                        findViewById<TextView>(R.id.mensagemTexto).text = "Erro ao obter localização"
                    }
                }
            })
        }

        private fun showExternalNotification(message: String) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.imagem)
                .setContentTitle("Info. Circular")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
                .setAutoCancel(true)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
        }

        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap
            val ufrpe = LatLng(-8.017335662754297, -34.949115979298256)

            try {
                val success = mMap.setMapStyle(com.google.android.gms.maps.model.MapStyleOptions.loadRawResourceStyle(this, R.raw.estilo_maps))
                if (!success) {
                    Log.e("Maps", "Falha ao carregar estilo do mapa. Verifique o arquivo estilo_maps.json")
                }
            } catch (e: android.content.res.Resources.NotFoundException) {
                Log.e("Maps", "Arquivo de estilo não encontrado: estilo_maps.json", e)
                Toast.makeText(this, "Erro ao aplicar estilo de mapa. Verifique o arquivo estilo_maps.json", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("Maps", "Erro inesperado ao carregar estilo de mapa", e)
                Toast.makeText(this, "Erro inesperado ao aplicar estilo de mapa", Toast.LENGTH_LONG).show()
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ufrpe, currentZoomLevel))

            val uiSettings = mMap.uiSettings
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isZoomGesturesEnabled = true

            addFixedMarkers()
        }

        private fun addFixedMarkers() {
            val locais = listOf(
                LatLng(-8.018405587113017, -34.95003668228588) to "Cegoe",
                LatLng(-8.015908378317642, -34.950759580760014) to "Guarita Piscina",
                LatLng(-8.014528748288745, -34.95043753092098) to "Reitoria",
                LatLng(-8.01621201175587, -34.94950232571275) to "Biblioteca Setorial",
                LatLng(-8.017728749495607, -34.94478152968631) to "Ceagri Computação",
                LatLng(-8.020046094447412, -34.95411159356041) to "Zootecnia",
                LatLng(-8.015024, -34.947718) to "Veterinária",
                LatLng(-8.014025, -34.948765) to "Biblioteca Central"
            )
            val width = 155
            val height = 155
            locais.forEach { (posicao, titulo) ->
                val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.icone_parada)
                val resizedIcon = Bitmap.createScaledBitmap(originalBitmap, width, height, false)
                mMap.addMarker(
                    MarkerOptions()
                        .position(posicao)
                        .title(titulo)
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedIcon))
                )
            }
        }
    }