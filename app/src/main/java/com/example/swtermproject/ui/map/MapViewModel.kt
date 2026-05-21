package com.example.swtermproject.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.model.PlaceModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MapViewModel : ViewModel() {

    companion object {
        private const val TAG = "MapViewModel"
    }

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
    fun getCurrentLocation(context: Context, useFine: Boolean) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val priority = if (useFine) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val tokenSource = CancellationTokenSource()
        fusedClient.getCurrentLocation(priority, tokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    _currentLocation.value = LatLng(location.latitude, location.longitude)
                    searchNearby(context, "hospital")
                } else {
                    requestSingleLocationUpdate(context, priority)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "getCurrentLocation failed", e)
                requestSingleLocationUpdate(context, priority)
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestSingleLocationUpdate(context: Context, priority: Int) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val request = LocationRequest.Builder(priority, 5000)
            .setMaxUpdates(1)
            .build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                fusedClient.removeLocationUpdates(this)
                val location = result.lastLocation ?: return
                _currentLocation.value = LatLng(location.latitude, location.longitude)
                searchNearby(context, "hospital")
            }
        }
        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }

    fun searchNearby(context: Context, placeType: String) {
        val currentLatLng = _currentLocation.value ?: return

        if (!Places.isInitialized()) {
            if (BuildConfig.MAPS_API_KEY.isBlank()) {
                Log.e(TAG, "MAPS_API_KEY is missing")
                _places.value = emptyList()
                return
            }
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
                Log.e(TAG, "searchNearby failed for $placeType", e)
                _places.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
