package com.example.pickandroll

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class GameListAdapter(private val games: List<Game>, private var userLocation: Location?, private val context: Context)
    : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    class ViewHolder(val layout: ConstraintLayout, val context: Context) : RecyclerView.ViewHolder(layout), View.OnClickListener {
        private val TAG = "GameListViewHolder"
        init{ layout.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val downloadIntent = Intent(context, GamePage::class.java)
            context.startActivity(downloadIntent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val gameView = LayoutInflater.from(parent.context).inflate(R.layout.games_view, parent, false) as ConstraintLayout
        return ViewHolder(gameView, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.layout.findViewById<TextView>(R.id.gameTitle).text = game.title

        val userLocation = this.userLocation
        if (userLocation != null) {
            val results = floatArrayOf(0f)
            Location.distanceBetween(userLocation.latitude, userLocation.longitude, game.location.latitude, game.location.longitude, results)
            val distance = metersToMiles(results[0])
            val decFormat = DecimalFormat("#.#")
            holder.layout.findViewById<TextView>(R.id.distanceText).text = context.resources.getString(R.string.distance_text, decFormat.format(distance))
        }

        holder.layout.findViewById<TextView>(R.id.participantsText).text = context.resources.getString(
            R.string.participants_text, game.curParticipants.toString(), game.maxParticipants.toString())
    }

    override fun getItemCount() = games.size

    fun setUserLocation(location: Location) {
        userLocation = location
    }
}
