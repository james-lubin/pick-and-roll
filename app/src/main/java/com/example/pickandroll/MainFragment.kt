package com.example.pickandroll

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pickandroll.databinding.FragmentMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        setUpGamesList()
        setUpMap()
        // Inflate the layout for this fragment
        return binding.root//inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = MainFragment().apply {}
    }

    private fun setUpGamesList() {
        val viewManager = LinearLayoutManager(context)
        gamesListAdapter = GameListAdapter(null, requireContext())
        val mainModel: MainViewModel by viewModels()
        mainModel.games.observe(requireActivity(), { gamesListAdapter.setItems(it) })
        mainModel.location.observe(requireActivity(), { updateViewWithLocation(it) })
        binding.gameList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gamesListAdapter
        }
    }

    private fun setUpMap() {
        val mapFragment = activity?.supportFragmentManager?.findFragmentById(R.id.mapFragment)
        if (mapFragment != null) {
            mapFragment as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
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
        }
    }
}