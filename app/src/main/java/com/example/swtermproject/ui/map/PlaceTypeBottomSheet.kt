package com.example.swtermproject.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_place_types.view.*

class PlaceTypeBottomSheet : BottomSheetDialogFragment() {

    var onTypeSelected: ((String) -> Unit)? = null

    private lateinit var adapter: PlaceTypeAdapter

    private val placeTypes = listOf(
        "Accounting" to "accounting",
        "Airport" to "airport",
        "Amusement Park" to "amusement_park",
        "Aquarium" to "aquarium",
        "Art Gallery" to "art_gallery",
        "ATM" to "atm",
        "Bakery" to "bakery",
        "Bank" to "bank",
        "Bar" to "bar",
        "Beauty Salon" to "beauty_salon",
        "Bicycle Store" to "bicycle_store",
        "Book Store" to "book_store",
        "Bowling Alley" to "bowling_alley",
        "Bus Station" to "bus_station",
        "Cafe" to "cafe",
        "Campground" to "campground",
        "Car Dealer" to "car_dealer",
        "Car Rental" to "car_rental",
        "Car Repair" to "car_repair",
        "Car Wash" to "car_wash",
        "Casino" to "casino",
        "Cemetery" to "cemetery",
        "Church" to "church",
        "City Hall" to "city_hall",
        "Clothing Store" to "clothing_store",
        "Convenience Store" to "convenience_store",
        "Courthouse" to "courthouse",
        "Dentist" to "dentist",
        "Department Store" to "department_store",
        "Doctor" to "doctor",
        "Drugstore" to "drugstore",
        "Electrician" to "electrician",
        "Electronics Store" to "electronics_store",
        "Embassy" to "embassy",
        "Fire Station" to "fire_station",
        "Florist" to "florist",
        "Funeral Home" to "funeral_home",
        "Furniture Store" to "furniture_store",
        "Gas Station" to "gas_station",
        "Gym" to "gym",
        "Hair Care" to "hair_care",
        "Hardware Store" to "hardware_store",
        "Hindu Temple" to "hindu_temple",
        "Home Goods Store" to "home_goods_store",
        "Hospital" to "hospital",
        "Insurance Agency" to "insurance_agency",
        "Jewelry Store" to "jewelry_store",
        "Laundry" to "laundry",
        "Lawyer" to "lawyer",
        "Library" to "library",
        "Light Rail Station" to "light_rail_station",
        "Liquor Store" to "liquor_store",
        "Local Government Office" to "local_government_office",
        "Locksmith" to "locksmith",
        "Lodging" to "lodging",
        "Meal Delivery" to "meal_delivery",
        "Meal Takeaway" to "meal_takeaway",
        "Mosque" to "mosque",
        "Movie Rental" to "movie_rental",
        "Movie Theater" to "movie_theater",
        "Moving Company" to "moving_company",
        "Museum" to "museum",
        "Night Club" to "night_club",
        "Painter" to "painter",
        "Park" to "park",
        "Parking" to "parking",
        "Pet Store" to "pet_store",
        "Pharmacy" to "pharmacy",
        "Physiotherapist" to "physiotherapist",
        "Plumber" to "plumber",
        "Police" to "police",
        "Post Office" to "post_office",
        "Primary School" to "primary_school",
        "Real Estate Agency" to "real_estate_agency",
        "Restaurant" to "restaurant",
        "Roofing Contractor" to "roofing_contractor",
        "RV Park" to "rv_park",
        "School" to "school",
        "Secondary School" to "secondary_school",
        "Shoe Store" to "shoe_store",
        "Shopping Mall" to "shopping_mall",
        "Spa" to "spa",
        "Stadium" to "stadium",
        "Storage" to "storage",
        "Store" to "store",
        "Subway Station" to "subway_station",
        "Supermarket" to "supermarket",
        "Synagogue" to "synagogue",
        "Taxi Stand" to "taxi_stand",
        "Tourist Attraction" to "tourist_attraction",
        "Train Station" to "train_station",
        "Transit Station" to "transit_station",
        "Travel Agency" to "travel_agency",
        "University" to "university",
        "Veterinary Care" to "veterinary_care",
        "Zoo" to "zoo"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_place_types, container, false)

        adapter = PlaceTypeAdapter(placeTypes) { _, type ->
            onTypeSelected?.invoke(type)
            dismiss()
        }

        view.rvPlaceTypes.layoutManager = LinearLayoutManager(context)
        view.rvPlaceTypes.adapter = adapter

        view.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })

        return view
    }
}
