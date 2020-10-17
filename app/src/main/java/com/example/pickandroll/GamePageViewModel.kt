package com.example.pickandroll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;

class GamePageViewModel : ViewModel() {

    fun getGamePageName(): LiveData<String> {
        return MutableLiveData("North Hills Run")
    }

    fun getCompetitionLevel(): LiveData<String> {
        return MutableLiveData("Semi-Pro")
    }

    fun getGamePageParticipants(): LiveData<String> {
        return MutableLiveData("6 / 8")
    }

    fun getGamePageGender(): LiveData<String> {
        return MutableLiveData("Mixed")
    }

    fun getGamePageDistance(): LiveData<String> {
        return MutableLiveData("3.2 mi")
    }

    fun getGamePageType(): LiveData<String> {
        return MutableLiveData("Full Court")
    }

    fun getGamePageLength(): LiveData<String> {
        return MutableLiveData("4 hours")
    }

    fun getGamePageNotes(): LiveData<String?> {
        return MutableLiveData(null)
    }
}
