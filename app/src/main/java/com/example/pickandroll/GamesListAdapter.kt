package com.example.pickandroll

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class GamesListAdapter(private val games: List<Game>, private var userLocation: Location?) :
    RecyclerView.Adapter<GamesListAdapter.MyViewHolder>() {

    class MyViewHolder(val constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesListAdapter.MyViewHolder {
        val gameView = LayoutInflater.from(parent.context).inflate(R.layout.games_view, parent, false) as ConstraintLayout
        return MyViewHolder(gameView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val game = games[position]
        holder.constraintLayout.findViewById<TextView>(R.id.titleTextView).text = game.title

        val userLocation = this.userLocation
        if (userLocation != null) {
            val results = floatArrayOf(0f)
            Location.distanceBetween(userLocation.latitude, userLocation.longitude, game.location.latitude, game.location.longitude, results)
            val distance = metersToMiles(results[0])
            val decFormat = DecimalFormat("#.#")
            holder.constraintLayout.findViewById<TextView>(R.id.distanceTextView).text = decFormat.format(distance) + "mi"
        }
    }

    override fun getItemCount() = games.size

    public fun setUserLocation(location: Location) {
        userLocation = location
    }
}
