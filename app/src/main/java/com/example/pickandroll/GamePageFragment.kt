package com.example.pickandroll

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pickandroll.databinding.FragmentGamePageBinding
import java.text.DecimalFormat

class GamePageFragment : Fragment() {
    private lateinit var binding: FragmentGamePageBinding
    private val mainModel: MainViewModel by activityViewModels()
    private var location: Location? = null

    companion object {
        @JvmStatic
        fun newInstance() = GamePageFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGamePageBinding.inflate(layoutInflater)
        mainModel.location.observe(viewLifecycleOwner, { it ?.let { location = it}})
        mainModel.selectedGame.observe(viewLifecycleOwner, { setDetails(it) })
        return binding.root
    }

    private fun setDetails(selectedGame: Game?) {
        selectedGame ?.let {
            binding.gamePageName.text = it.title
            binding.competitionLevelText.text = it.competitionLevel.toString()
            binding.gamePageParticipants.text = it.curParticipants.toString()
            binding.gamePageGender.text = it.genderRule.toString()
            binding.gamePageLength.text = it.lengthInHours.toString()

            if (it.type != null) {
                binding.gamePageType.text = it.type.toString()
            }

            if (it.photoUrl != null) {
                binding.gameImage.setPadding(0);
                Glide.with(this).load(it.photoUrl).centerCrop().into(binding.gameImage)
            }

            if (it.notes == null) {
                binding.gamePageNotes.text = "No notes found." //TODO: Italicize text
            } else {
                binding.gamePageNotes.text = it.notes
            }

            location ?.let { loc ->
                val results = floatArrayOf(0f)
                Location.distanceBetween(loc.latitude, loc.longitude, it.location.latitude, it.location.longitude, results)
                val distance = metersToMiles(results[0])
                val decFormat = DecimalFormat("#.#")
                binding.gamePageDistance.text = requireContext().resources.getString(R.string.distance_text, decFormat.format(distance))
            }
        }
    }
}