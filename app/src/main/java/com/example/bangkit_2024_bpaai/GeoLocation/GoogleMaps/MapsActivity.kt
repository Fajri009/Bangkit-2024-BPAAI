package com.example.bangkit_2024_bpaai.GeoLocation.GoogleMaps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.GeoLocation.GoogleMaps.data.TourismPlace
import com.example.bangkit_2024_bpaai.GeoLocation.GoogleMaps.util.vectorToBitmap
import com.example.bangkit_2024_bpaai.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.example.bangkit_2024_bpaai.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Menampilkan Marker dan Mengatur Animasi
        addMarker()

        // menambahkan marker sesuai dengan lokasi yang dipilih oleh penguna
        addMarkerCustomIcon()

        // mengunakan icon yang sudah ada dan cukup mengganti warnanya saja
        // menambahkan marker sesuai dengan point of interest yang dipilih pengguna
        addMarkerCustomColor()

        // Untuk menampilkan beberapa kontrol
        mMap.uiSettings.isZoomControlsEnabled = true // zoomControlsEnabled : Mengaktifkan zoom control pada pojok kanan bawah.
        mMap.uiSettings.isIndoorLevelPickerEnabled = true // indoorLevelPickerEnabled : Mengaktifkan fitur untuk menampilkan detail lantai.
        mMap.uiSettings.isCompassEnabled = true // compassEnabled : Mengaktifkan kompas pada pojok kiri atas.
        mMap.uiSettings.isMapToolbarEnabled = true // mapToolbarEnabled : Mengaktifkan toolbar untuk navigasi dan aplikasi Google Maps.
        /*
            scrollGesturesEnabled, setTiltGesturesEnabled, setZoomGesturesEnabled, setRotateGesturesEnabled, dan setAllGesturesEnabled : Mengaktifkan beberapa atau semua gerakan gestur tangan seperti scroll untuk memindahkan tampilan peta, tilt untuk memiringkan peta (efek 3D), pinch untuk zoom, dan rotate untuk memutar arah peta.
         */

        // Menampilkan My Location
        getMyLocation()

        // Mengubah Style pada Peta
        setMapStyle()

        // Menampilkan Banyak Marker dengan LatLngBounds
        addManyMarker()
    }

    // untuk menampilkan menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    // untuk menangani aksi ketika menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun addMarker() {
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) // moveCamera untuk mengarahkan kamera ke suatu posisi

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.addMarker(
            MarkerOptions()
                .position(dicodingSpace) // untuk menampung data Latitude dan Longitude
                .title("Dicoding Space") // Teks yang muncul pada Info Window ketika marker dipilih
                .snippet("Batik Kumeli No.50") // Teks tambahan yang muncul di bawah title
                /*
                    Icon : Bitmap yang digunakan untuk menggantikan default marker.
                    Anchor : Bagian gambar yang diletakkan pada posisi koordinat. Secara default, ada di bawah tengah gambar.
                    Alpha : Menentukan seberapa transparan marker yang ditampilkan. Dapat diisi dengan angka 0-1.0.
                    Draggable : Menentukan apakah marker dapat di-drag (digeser) atau tidak.
                    Visible : Menentukan apakah marker dapat dilihat atau tidak.
                 */
        )

        // animateCamera untuk membuat animasi ke lokasi tersebut
        // newLatLngZoom untuk memperbesar peta dengan level 15
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
        /*
            newLatLng : Menampilkan bagian tengah maps ke suatu titik koordinat.
            newLatLngZoom : Memperbesar maps ke suatu titik koordinat dengan zoom level yang bisa ditentukan.
            newLatLngBounds : Memperbesar maps agar semua titik koordinat yang ditentukan terlihat. Cocok digunakan untuk menampilkan banyak posisi yang ingin ditampilkan pada suatu area, misal lokasi rumah sakit, pom bensin, dsb.
         */
    }

    private fun addMarkerCustomIcon() {
        /*
            setOnMapClickListener : Menangani aksi ketika map diklik.
            setOnMapLongClickListener : Menangani aksi ketika map diklik lama.
            setOnPoiClickListener : Menangani aksi ketika titik POI dipilih.
            setOnMarkerClickListener : Menangani aksi ketika marker dipilih.
            setOnInfoWindowClickListener : Menangani aksi ketika info window dipilih.
         */
        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("New Marker")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#3DDC84"), resources))
            )
        }
    }

    private fun addMarkerCustomColor() {
        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
                // loadRawResourceStyle digunakan untuk mengambil resource dari folder raw

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun addManyMarker() {
        val tourismPlace = listOf(
            TourismPlace("Floating Market Lembang", -6.8168954,107.6151046),
            TourismPlace("The Great Asia Africa", -6.8331128,107.6048483),
            TourismPlace("Rabbit Town", -6.8668408,107.608081),
            TourismPlace("Alun-Alun Kota Bandung", -6.9218518,107.6025294),
            TourismPlace("Orchid Forest Cikole", -6.780725, 107.637409),
        )

        tourismPlace.forEach { tourism ->
            val latLng = LatLng(tourism.latitude, tourism.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name))
            boundsBuilder.include(latLng) // Untuk mengumpulkan setiap titik yang ingin terlihat, gunakan fungsi include pada LatLngBounds.Builder
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels, // displayMetrics untuk mengetahui seberapa lebar dan tinggi peta yang akan tampil
                resources.displayMetrics.heightPixels,
                300 // Padding merupakan jarak antara semua titik dengan pinggir peta sehingga tampilan tidak terlalu mepet
            )
        )
    }

    //use live template logt to create this
    companion object {
        private const val TAG = "MapsActivity"
    }
}

