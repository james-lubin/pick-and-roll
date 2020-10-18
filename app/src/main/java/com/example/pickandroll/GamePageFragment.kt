package com.example.pickandroll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GamePageFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = GamePageFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_page, container, false)
    }
}