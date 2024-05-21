package com.example.bangkit_2024_bpaai.GeoLocation.GoogleMaps.util

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.*

// konversi vektor ke bitmap
fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int, resources: Resources): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)

    if (vectorDrawable == null) {
        Log.e("BitmapHelper", "Resource not found")
        return BitmapDescriptorFactory.defaultMarker()
    }

    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    DrawableCompat.setTint(vectorDrawable, color)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
