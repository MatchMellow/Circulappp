package com.example.CirculAPP

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.example.CirculAPP.databinding.ActivityMapsMotoristaBinding
import android.content.Context
import android.content.res.Configuration

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsMotoristaBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var databaseRef: DatabaseReference
    private var motoristaMarker: Marker? = null
    private var rotaEscolhida: String = "Não definida"
    private var zoomLevel: Float = 16f
    private var isPositionLocked = false

    private var lastLatitude: Double = 0.0
    private var lastLongitude: Double = 0.0

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Permissão negada. Não será possível obter a localização!", Toast.LENGTH_SHORT).show()
            }
        }

    // **** NOVO CÓDIGO AQUI: Trava a escala da fonte ****
    override fun attachBaseContext(newBase: Context) {
        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = 1.0f // Define a escala da fonte para 1.0f (tamanho padrão)
        val context = newBase.createConfigurationContext(newConfig)
        super.attachBaseContext(context)
    }
    // **** FIM DO NOVO CÓDIGO ****

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsMotoristaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        databaseRef = FirebaseDatabase.getInstance().getReference("motorista")

        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
        zoomLevel = sharedPreferences.getFloat("zoomLevel", 16f)

        rotaEscolhida = intent?.getStringExtra("rota") ?: "Não definida"
        Log.d("MapsActivity", "Rota escolhida recebida: $rotaEscolhida")
        salvarRotaNoFirebase(rotaEscolhida)

        binding.txtStatusMotorista.text = "🚍 Você está fazendo a rota: $rotaEscolhida!"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapMotorista) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        checkLocationPermission()

        binding.btnEncerrarRota.setOnClickListener {
            binding.txtStatusMotorista.text = "❌ Você está offline!"
            encerrarRota()
        }

        binding.btnCentralizarCircular.setOnClickListener {
            motoristaMarker?.position?.let { position ->
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
            } ?: run {
                Toast.makeText(this, "A posição do circular ainda não foi definida!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnTravarPosicao.setOnClickListener {
            isPositionLocked = !isPositionLocked

            if (isPositionLocked) {
                Toast.makeText(this, "Posição Travada! A tela continuará focada no motorista.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Posição destravada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun salvarRotaNoFirebase(rota: String) {
        val rotaAtivaRef = databaseRef.child("rotaAtiva")
        val dadosRota = mapOf(
            "horario" to rota,
            "status" to "ativa"
        )
        rotaAtivaRef.setValue(dadosRota)
            .addOnSuccessListener { Log.d("Firebase", "Rota ativa salva: $rota") }
            .addOnFailureListener { e -> Log.e("Firebase", "Erro ao salvar a rota: ${e.message}") }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val ufrpe = LatLng(-8.017335662754297, -34.949115979298256)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ufrpe, zoomLevel))

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
            mMap.addMarker(MarkerOptions()
                .position(posicao)
                .title(titulo)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedIcon))
            )
        }
    }

    private fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c * 1000
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for (location in locationResult.locations) {
                val userLocation = LatLng(location.latitude, location.longitude)

                if (calcularDistancia(lastLatitude, lastLongitude, location.latitude, location.longitude) > 25) {
                    runOnUiThread {
                        if (motoristaMarker == null) {
                            motoristaMarker = mMap.addMarker(
                                MarkerOptions()
                                    .position(userLocation)
                                    .title("Circular Está Aqui")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_motorista))
                            )
                        } else {
                            motoristaMarker?.position = userLocation
                        }

                        databaseRef.child("localizacao").setValue(
                            mapOf(
                                "latitude" to location.latitude,
                                "longitude" to location.longitude,
                                "status" to "em rota",
                                "rota" to rotaEscolhida
                            )
                        )
                    }

                    lastLatitude = location.latitude
                    lastLongitude = location.longitude
                }

                // Movimenta o mapa
                if (isPositionLocked) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel))
                }
            }
        }
    }

    private fun encerrarRota() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        databaseRef.child("rotaAtiva").setValue(mapOf("horario" to null, "status" to "inativa"))
        databaseRef.child("localizacao").setValue(mapOf("latitude" to null, "longitude" to null, "status" to "indisponível", "rota" to null))

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("startDestination", "motoristaOptions")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}