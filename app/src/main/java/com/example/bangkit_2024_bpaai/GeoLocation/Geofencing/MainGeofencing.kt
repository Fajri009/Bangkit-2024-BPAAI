package com.example.bangkit_2024_bpaai.GeoLocation.Geofencing

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.GeoLocation.Geofencing.receiver.GeofenceBroadcastReceiver
import com.example.bangkit_2024_bpaai.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.bangkit_2024_bpaai.databinding.ActivityMainGeofencingBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions

class MainGeofencing : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainGeofencingBinding
    private val centerLat = 37.4274745
    private val centerLng = -122.169719
    private val geofenceRadius = 400.0

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private lateinit var geofencingClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainGeofencingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        val stanford = LatLng(centerLat, centerLng)
        mMap.addMarker(MarkerOptions().position(stanford).title("Stanford University"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stanford, 15f))

        mMap.addCircle(
            CircleOptions()
                .center(stanford)
                .radius(geofenceRadius)
                .fillColor(0x22FF0000)
                .strokeColor(Color.RED)
                .strokeWidth(3f)
        )

        getMyLocation()
        addGeofence()
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Apabila permission ACCESS_FINE_LOCATION terpenuhi, maka langkah selanjutnya yaitu meminta permission untuk ACCESS_BACKGROUND_LOCATION apabila aplikasi berjalan di Android 10 (Q) ke atas.
                getMyLocation()
            }
        }

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // untuk membuat pending intent ke BroadcastcastReceiver
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        geofencingClient = LocationServices.getGeofencingClient(this)

        val geofence = Geofence.Builder()
            .setRequestId("kampus") // Digunakan sebagai id untuk membedakan setiap geofence
            .setCircularRegion( // Berisi tiga parameter untuk menentukan area, yakni latitude, longitude, dan radius
                centerLat,
                centerLng,
                geofenceRadius.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE) // Menentukan masa kadaluarsa dari geofence yang dibuat dalam milidetik
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER) // Menentukan aksi apa yang ingin dibaca pada Geofence
            .setLoiteringDelay(5000) // Menentukan seberapa lama waktu yang digunakan untuk mengetahui sebuah device dikatakan tinggal (dwell) pada suatu area dalam milidetik
            // setNotificationResponsiveness : Menentukan seberapa responsif sistem memberitahu aplikasi jika terdapat suatu aksi dalam milidetik
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER) // Menentukan trigger awal apabila device sudah di dalam area ketika Geofence ditambahkan
            .addGeofence(geofence) // Menambahkan sebuah Geofence
            // addGeofences : Menambahkan list Geofence
            .build()

        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                    addOnFailureListener {
                        showToast("Geofencing not added : ${it.message}")
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@MainGeofencing, text, Toast.LENGTH_SHORT).show()
    }
}