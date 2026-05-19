package com.example.swtermproject.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceModel(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val rating: Float = 0f,
    val category: String = ""
) : Parcelable
