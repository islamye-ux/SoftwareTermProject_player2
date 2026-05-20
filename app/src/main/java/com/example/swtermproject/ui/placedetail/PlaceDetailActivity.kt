package com.example.swtermproject.ui.placedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.swtermproject.R
import com.example.swtermproject.data.model.PlaceModel
import com.example.swtermproject.data.repository.FavoriteRepository
import com.example.swtermproject.databinding.ActivityPlaceDetailBinding
import com.example.swtermproject.util.Constants
import kotlinx.coroutines.launch

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var place: PlaceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteRepository = FavoriteRepository(this)

        place = PlaceModel(
            id = intent.getStringExtra(Constants.EXTRA_PLACE_ID) ?: "",
            name = intent.getStringExtra(Constants.EXTRA_PLACE_NAME) ?: "",
            address = intent.getStringExtra(Constants.EXTRA_PLACE_ADDRESS) ?: "",
            lat = intent.getDoubleExtra(Constants.EXTRA_PLACE_LAT, 0.0),
            lng = intent.getDoubleExtra(Constants.EXTRA_PLACE_LNG, 0.0),
            rating = intent.getFloatExtra(Constants.EXTRA_PLACE_RATING, 0f),
            category = intent.getStringExtra(Constants.EXTRA_PLACE_CATEGORY) ?: ""
        )

        setupToolbar()
        bindPlaceData()
        refreshFavoriteIcon()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = place.name
        }
    }

    private fun bindPlaceData() {
        binding.tvName.text = place.name
        binding.tvAddress.text = place.address
        binding.tvRating.text = if (place.rating > 0) "★ ${place.rating}" else getString(R.string.no_rating)
    }

    private fun refreshFavoriteIcon() {
        lifecycleScope.launch {
            val isFav = favoriteRepository.isFavorite(place.id)
            binding.fabFavorite.setImageResource(
                if (isFav) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
    }

    private fun setupClickListeners() {
        binding.fabFavorite.setOnClickListener {
            lifecycleScope.launch {
                if (favoriteRepository.isFavorite(place.id)) {
                    favoriteRepository.removeFavorite(place.id)
                    binding.fabFavorite.setImageResource(android.R.drawable.btn_star_big_off)
                    Toast.makeText(this@PlaceDetailActivity, R.string.place_removed, Toast.LENGTH_SHORT).show()
                } else {
                    favoriteRepository.addFavorite(place)
                    binding.fabFavorite.setImageResource(android.R.drawable.btn_star_big_on)
                    Toast.makeText(this@PlaceDetailActivity, R.string.place_saved, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDirections.setOnClickListener {
            val uri = Uri.parse("google.navigation:q=${place.lat},${place.lng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val webUri = Uri.parse("https://maps.google.com/?q=${place.lat},${place.lng}")
                startActivity(Intent(Intent.ACTION_VIEW, webUri))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return true
    }
}
