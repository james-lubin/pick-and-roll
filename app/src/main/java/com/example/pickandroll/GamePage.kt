package com.example.pickandroll

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pickandroll.databinding.ActivityGamePageBinding

class GamePage : AppCompatActivity() {

    private lateinit var binding: ActivityGamePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page)
        binding = ActivityGamePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        val gamePageModel: GamePageViewModel by viewModels()
        gamePageModel.getGamePageName().observe(this, { binding.gamePageName.text = it })
        gamePageModel.getCompetitionLevel().observe(this, { binding.competitionLevelText.text = it })
        gamePageModel.getGamePageParticipants().observe(this, { binding.gamePageParticipants.text = it })
        gamePageModel.getGamePageGender().observe(this, { binding.gamePageGender.text = it })
        gamePageModel.getGamePageDistance().observe(this, { binding.gamePageDistance.text = it })
        gamePageModel.getGamePageType().observe(this, { binding.gamePageType.text = it })
        gamePageModel.getGamePageLength().observe(this, { binding.gamePageLength.text = it })
        gamePageModel.getGamePageNotes().observe(this, { binding.gamePageNotes.text = it })
    }
}