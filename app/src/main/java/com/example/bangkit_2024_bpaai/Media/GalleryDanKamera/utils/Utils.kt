package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.utils

import android.content.*
import android.graphics.*
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.example.bangkit_2024_bpaai.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

// untuk menampung hasil foto dari kamera bawaan dan mendapatkan nilai Uri dari file tersebut

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
private const val MAXIMAL_SIZE = 1000000

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    // Pada Android Q (API Level 29) ke atas, Anda bisa menggunakan MediaStore untuk membuat File dan mendapatkan Uri tanpa perlu permission apa pun
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // ContentValues berfungsi untuk menyimpan data dalam bentuk key-value sebelum dimasukkan ke dalam contentResolver.
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            // EXTERNAL_CONTENT_URI supaya data disimpan di external storage, sehingga data tersebut dapat diakses oleh aplikasi lain atau oleh aplikasi galeri foto
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    // getExternalFilesDir() untuk mendapatkan lokasi external storage
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

// File tempat foto hasil dari kamera disimpan
fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

// untuk mengubah Uri menjadi File
fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    // InputStream ini akan digunakan untuk membaca data dari Uri gambar
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100 // Semakin besar file gambar, semakin menurun pula nilainya
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
    // true jika ingin gambar yang dibuat diberi interpolasi untuk hasil yang lebih baik
}