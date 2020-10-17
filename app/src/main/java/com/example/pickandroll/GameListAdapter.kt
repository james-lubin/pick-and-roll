package com.example.pickandroll

import android.content.Context
import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pickandroll.databinding.ActivityMainBinding
import com.example.pickandroll.databinding.GamesViewBinding
import java.text.DecimalFormat

class GameListAdapter(private var userLocation: Location?, private val context: Context)
    : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    private val games: MutableList<Game> = mutableListOf()

    override fun getItemCount() = games.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GamesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.binding.gameTitle.text = game.title

        val userLocation = this.userLocation
        if (userLocation != null) {
            val results = floatArrayOf(0f)
            Location.distanceBetween(userLocation.latitude, userLocation.longitude, game.location.latitude, game.location.longitude, results)
            val distance = metersToMiles(results[0])
            val decFormat = DecimalFormat("#.#")
            holder.binding.distanceText.text = context.resources.getString(R.string.distance_text, decFormat.format(distance))
        }

        holder.binding.participantsText.text = context.resources.getString(R.string.participants_text, game.curParticipants.toString(), game.maxParticipants.toString())
    }

    fun setItems(games: List<Game>) {
        this.games.clear()
        this.games.addAll(games)
        notifyDataSetChanged()
    }

    fun setUserLocation(location: Location) {
        userLocation = location
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: GamesViewBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init{ binding.root.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val downloadIntent = Intent(context, GamePage::class.java)
            context.startActivity(downloadIntent)

        }
    }
}
