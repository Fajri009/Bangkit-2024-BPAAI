package com.example.bangkit_2024_bpaai.Animation.ActivityAnimation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hero(
    var name: String,
    var description: String,
    var photo: String
) : Parcelable