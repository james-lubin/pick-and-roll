package com.example.pickandroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.pickandroll.databinding.ActivityMainBinding
import io.nlopez.smartlocation.SmartLocation

private const val PERMISSION_REQUEST_LOCATION_COARSE: Int = 0
private const val REQUEST_CHECK_SETTINGS: Int = 1

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        val viewModel: MainViewModel by viewModels()
        SmartLocation.with(this).location().oneFix().start { viewModel.location.value = it }
    }
}