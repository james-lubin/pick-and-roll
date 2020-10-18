package com.example.pickandroll

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pickandroll.databinding.GamesViewBinding
import java.text.DecimalFormat

class GameListAdapter(private var userLocation: Location?, private val clickListener: OnClickListener, private val context: Context) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    private val games: MutableList<Game> = mutableListOf()

    override fun getItemCount() = games.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GamesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.binding.gameTitle.text = game.title
        holder.game = game

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

    class ViewHolder(val binding: GamesViewBinding, private val clickListener: OnClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init{ binding.root.setOnClickListener(this) }
        var game: Game? = null

        override fun onClick(view: View?) {
            game?.let { clickListener.onClick(it) }
            binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToGamePageFragment())
        }
    }

    interface OnClickListener {
        fun onClick(game: Game)
    }
}
