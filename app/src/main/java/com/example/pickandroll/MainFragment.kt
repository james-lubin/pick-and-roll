package com.example.pickandroll

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pickandroll.databinding.FragmentMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "MainFragment"
    private lateinit var gamesListAdapter: GameListAdapter
    private lateinit var binding: FragmentMainBinding
    private var map: GoogleMap? = null

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        setUpGamesList()
        setUpMap()
        return binding.root
    }

    private fun setUpGamesList() {
        val viewManager = LinearLayoutManager(context)
        gamesListAdapter = GameListAdapter(null, requireContext())
        val mainModel: MainViewModel by activityViewModels()
        mainModel.games.observe(viewLifecycleOwner, { gamesListAdapter.setItems(it) })
        mainModel.location.observe(viewLifecycleOwner, { updateViewWithLocation(it) })
        binding.gameList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gamesListAdapter
        }
    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentByTag("mapFragment") as SupportMapFragment
        Log.d(TAG, "Calling map async")
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        Log.d(TAG, "Got map")
    }

    private fun updateViewWithLocation(currentLocation: Location?) {
        Log.d(TAG, "updateViewWithLocation: called")
        currentLocation?.let {
            Log.d(TAG, "updateViewWithLocation: location is non null")
            setMapToCurrentLocation(it)
            gamesListAdapter.setUserLocation(it) }
    }

    private fun setMapToCurrentLocation(location: Location?) {
        if (location?.latitude != null) {
            val locationLatLng = LatLng(location.latitude, location.longitude)
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15f))
            Log.d(TAG, "Set map to: $locationLatLng")
        }
    }
}