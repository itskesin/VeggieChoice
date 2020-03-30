package com.veggiechoice.orbital

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.veggiechoice.orbital.model.Restaurant
import com.veggiechoice.orbital.util.RestaurantUtil
import kotlinx.android.synthetic.main.activity_add_restaurant.*
import java.util.*

class AddRestaurantActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    private val selectedCategory: String?
        get() {
            val selected = spinCategory.selectedItem as String
            return if (getString(R.string.value_any_category) == selected) {
                null
            } else {
                selected
            }
        }

    private val selectedCity: String?
        get() {
            val selected = spinCity.selectedItem as String
            return if (getString(R.string.value_any_city) == selected) {
                null
            } else {
                selected
            }
        }

    private val selectedPrice: Int
        get() {
            val selected = spinPrice.selectedItem as String
            return when (selected) {
                getString(R.string.price_1) -> 1
                getString(R.string.price_2) -> 2
                getString(R.string.price_3) -> 3
                else -> -1
            }
        }


    val restaurant: Restaurant
        get() {
            val restaurant = Restaurant()
            val random = Random()

            restaurant.category = selectedCategory
            restaurant.city = selectedCity
            restaurant.price = selectedPrice
            restaurant.name = restName.text.toString()
            restaurant.address = restAddress.text.toString()
            restaurant.hour = restHour.text.toString()
            restaurant.description = restDescription.text.toString()
            restaurant.photo = getRandomImageUrl(random)

            return restaurant
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        restaurantCancelButton.setOnClickListener { onBackArrowClicked() }
        restaurantSubmitButton.setOnClickListener { addRestaurantClicked() }
    }

    private fun addRestaurantClicked() {
        onRestaurant(restaurant)
        onBackPressed()
    }

    private fun onRestaurant(restaurant: Restaurant) {
        // Note: average rating intentionally not set

        val batch = firestore.batch()

        val restRef = firestore.collection("restaurants").document()


        // Add restaurant
        batch.set(restRef, restaurant)


        batch.commit().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Write batch succeeded.")
            } else {
                Log.w(TAG, "write batch failed.", task.exception)
            }
        }
    }

    private fun onBackArrowClicked() {
        onBackPressed()
    }

    private fun getRandomImageUrl(random: Random): String {

        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        val id = random.nextInt(RestaurantUtil.MAX_IMAGE_NUM) + 1

        return String.format(Locale.getDefault(), RestaurantUtil.RESTAURANT_URL_FMT, id)
    }


    companion object {

        private const val TAG = "AddRestaurantActivity"

    }
}