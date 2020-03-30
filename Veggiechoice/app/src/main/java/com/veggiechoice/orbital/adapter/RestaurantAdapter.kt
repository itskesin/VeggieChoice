package com.veggiechoice.orbital.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.veggiechoice.orbital.R
import com.veggiechoice.orbital.model.Restaurant
import com.veggiechoice.orbital.util.RestaurantUtil
import kotlinx.android.synthetic.main.item_restaurant.view.*

/**
 * RecyclerView adapter for a list of Restaurants.
 */
open class RestaurantAdapter(query: Query, private val listener: OnRestaurantSelectedListener) :
    FirestoreAdapter<RestaurantAdapter.ViewHolder>(query) {

    interface OnRestaurantSelectedListener {

        fun onRestaurantSelected(restaurant: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_restaurant, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnRestaurantSelectedListener?
        ) {

            val restaurant = snapshot.toObject(Restaurant::class.java)
            if (restaurant == null) {
                return
            }

            val resources = itemView.resources

            // Load image
            Glide.with(itemView.restaurantItemImage.context)
                .load(restaurant.photo)
                .into(itemView.restaurantItemImage)

            val numRatings: Int = restaurant.numRatings

            itemView.restaurantItemName.text = restaurant.name
            itemView.restaurantItemRating.rating = restaurant.avgRating.toFloat()
            itemView.restaurantItemCity.text = restaurant.city
            itemView.restaurantItemCategory.text = restaurant.category
            itemView.restaurantItemHour.text = restaurant.hour
            itemView.restaurantItemNumRatings.text = resources.getString(
                R.string.fmt_num_ratings,
                numRatings
            )
            itemView.restaurantItemPrice.text = RestaurantUtil.getPriceString(restaurant)

            // Click listener
            itemView.setOnClickListener {
                listener?.onRestaurantSelected(snapshot)
            }
        }
    }
}
