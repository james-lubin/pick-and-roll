package com.example.pickandroll.gameslistpage

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickandroll.game.Game
import com.example.pickandroll.R
import com.example.pickandroll.databinding.GamesViewBinding
import com.example.pickandroll.game.getDistance
import java.text.DecimalFormat

class GameListAdapter(private var userLocation: Location?, private val context: Context, private val clickListener: OnClickListener) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    private val games: MutableList<Game> = mutableListOf()

    override fun getItemCount() = games.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        var distanceText: String? = null
        userLocation?.let {
            val distance = getDistance(it, game)
            val decFormat = DecimalFormat("#.#")
            distanceText = context.resources.getString(R.string.distance_text, decFormat.format(distance))
        }

        val participantsText = context.resources.getString(R.string.participants_text, game.curParticipants.toString(), game.maxParticipants.toString())
        holder.bind(game, distanceText = distanceText, participantsText = participantsText)
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

    class ViewHolder private constructor(private val binding: GamesViewBinding, private val clickListener: OnClickListener)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init{ binding.root.setOnClickListener(this) }
        var game: Game? = null

        companion object {
            fun from(parent: ViewGroup, clickListener: OnClickListener): ViewHolder {
                val binding = GamesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding, clickListener)
            }
        }

        fun bind(game: Game, participantsText: String, distanceText: String?) {
            this.game = game
            binding.gameTitle.text = game.title
            binding.distanceText.text = distanceText
            binding.participantsText.text = participantsText
        }

        override fun onClick(view: View?) {
            game?.let { clickListener.onClick(it) }
        }
    }

    interface OnClickListener {
        fun onClick(game: Game)
    }
}
