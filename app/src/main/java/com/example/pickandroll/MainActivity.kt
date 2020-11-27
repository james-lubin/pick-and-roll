package com.example.pickandroll

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pickandroll.databinding.ActivityMainBinding
import io.nlopez.smartlocation.SmartLocation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        val viewModel: MainViewModel by viewModels()

        SmartLocation.with(this).location().start {
            viewModel.updateLocation(it)
        }
    }

    override fun onDestroy() {
        SmartLocation.with(this).location().stop();
        super.onDestroy()
    }
}