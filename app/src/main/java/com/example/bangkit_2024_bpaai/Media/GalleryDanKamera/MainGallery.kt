package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.api.ApiConfig
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.api.FileUploadResponse
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.utils.getImageUri
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.utils.reduceFileImage
import com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.utils.uriToFile
import com.example.bangkit_2024_bpaai.R
import com.example.bangkit_2024_bpaai.databinding.ActivityMainGalleryBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class MainGallery : AppCompatActivity() {
    private lateinit var binding: ActivityMainGalleryBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    // Gallery
    private fun startGallery() {
        // panggil fungsi launch pada startGallery() untuk menjalankan Photo Picker
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        /*
            VideoOnly: Untuk mendapatkan media video saja.
            ImageAndVideo: Untuk mendapatkan media gambar dan video.
            SingleMimeType("image/gif"): Untuk mendapatkan data dengan ekstensi tertentu, seperti gif pada contoh ini.
         */
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) { // Apabila null, artinya belum ada media yang dipilih
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // Selain itu, Anda juga dapat memilih banyak media sekaligus dengan menggunakan PickMultipleVisualMedia
    /*
    val pickMultipleMedia =
        registerForActivityResult(PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
     */

    // Jika Anda membutuhkannya dalam waktu lama, misal upload file berukuran besar di belakang layar, Anda perlu izin yang lebih lama
    /*
        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, flag)
     */

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    /*
        setImageURI(URI uri): digunakan untuk menampilkan gambar dari URI yang merupakan lokasi dari suatu file, biasanya berformat content://.
        setImageBitmap(Bitmap bitmap): digunakan jika Anda memerlukan gambar dalam bentuk Bitmap. Salah satu kelebihan file bitmap adalah Anda dapat mengatur skala dan orientasinya.
        setImageResource(int resId): digunakan jika Anda memiliki gambar dari res/drawable.
        setImageDrawable(Drawable drawable): digunakan untuk menampilkan gambar dari object Drawable.
     */

    // Camera
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    // CameraX
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    // memeriksa terlebih dahulu apakah izin tersebut sudah diberikan atau tidak
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    // requestPermissionLauncher digunakan untuk menanggapi setelah pop-up permission muncul
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    // launch digunakan untuk melakukan permintaan izin (request permission)
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = "Ini adalah deksripsi gambar"

            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            // untuk mengunggah gambar
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.uploadImage(multipartBody, requestBody)
                    showToast(successResponse.message)
                    showLoading(false)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                    showToast(errorResponse.message)
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}