package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.utils.createCustomTempFile
import com.example.bangkit_2024_bpaai.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        binding.captureImage.setOnClickListener { takePhoto() }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun startCamera() {
        // showCamera
        // untuk membuat instance secara singleton dari ProcessCameraProvider dengan memanggil fungsi getInstance
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // ProcessCameraProvider yang berfungsi untuk mengikat lifecycle kamera ke LifecycleOwner selama jalannya aplikasi
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // imageCapture yang nantinya digunakan pada function takePhoto untuk mengambil gambar dari kamera
            imageCapture = ImageCapture.Builder().build()

            // untuk memastikan bahwa kesalahan selama menghubungkan cameraProvider dengan cameraSelector dan preview
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // takePhoto
        val imageCapture = imageCapture ?: return

        // menyiapkan file yang akan digunakan untuk menampung hasil gambar dari kamera
        val photoFile = createCustomTempFile(application)

        // File ini digunakan untuk menjelaskan secara detail bagaimana output atau hasil dari kamera nantinya
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Selain menyimpan dalam file yang telah tersedia, kita bisa juga menyimpannya dalam MediaStore sehingga aplikasi lain dapat membuka gambar tersebut
        /*
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
                .build()
         */

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    setResult(CAMERAX_RESULT, intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    // agar orientasi yang dihasilkan foto sama dengan posisi handphone sekarang
    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                /*
                    0 Derajat (Portrait): Ini adalah orientasi potrait standar, dengan perangkat digunakan dalam posisi tegak (vertikal).
                    90 Derajat (Landscape): Ini adalah orientasi landscape standar, dengan perangkat digunakan dalam posisi mendatar (horizontal).
                    180 Derajat (Reverse Portrait): Ini adalah orientasi potrait terbalik, dengan perangkat digunakan dalam posisi tegak terbalik.
                    270 Derajat (Reverse Landscape): Ini adalah orientasi landscape terbalik, dengan perangkat digunakan dalam posisi mendatar terbalik.
                 */
                imageCapture?.targetRotation = rotation
            }
        }
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}