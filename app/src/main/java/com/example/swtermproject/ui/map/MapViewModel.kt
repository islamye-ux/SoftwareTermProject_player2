package com.example.swtermproject.ui.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.model.PlaceModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MapViewModel : ViewModel() {

    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng> = _currentLocation

    private val _places = MutableLiveData<List<PlaceModel>>(emptyList())
    val places: LiveData<List<PlaceModel>> = _places

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val placeFields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG,
        Place.Field.RATING
    )

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        fusedClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            _currentLocation.value = LatLng(location.latitude, location.longitude)
            searchNearby(context, "hospital")
        }
    }

    fun searchNearby(context: Context, placeType: String) {
        val currentLatLng = _currentLocation.value ?: return

        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, BuildConfig.MAPS_API_KEY)
        }
        val placesClient = Places.createClient(context)

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val circle = CircularBounds.newInstance(currentLatLng, 1500.0)
                val request = SearchNearbyRequest.builder(circle, placeFields)
                    .setIncludedTypes(listOf(placeType))
                    .setMaxResultCount(10)
                    .build()

                val response = placesClient.searchNearby(request).await()
                _places.value = response.places.mapNotNull { place ->
                    val latLng = place.latLng ?: return@mapNotNull null
                    PlaceModel(
                        id = place.id ?: "",
                        name = place.name ?: "",
                        address = place.address ?: "",
                        lat = latLng.latitude,
                        lng = latLng.longitude,
                        rating = (place.rating ?: 0.0).toFloat(),
                        category = placeType
                    )
                }
            } catch (e: Exception) {
                _places.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
