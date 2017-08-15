package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by boking on 2017-08-12.
 */
class FragmentMap : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        var mapView : MapView = root.findViewById(R.id.fragmentMapView) as MapView

        val bundle = this.arguments
        val generator : WorldGenerator = bundle.getSerializable("generator") as WorldGenerator

        mapView.mapTiles=generator.mapTiles
        mapView.updateMapSize(bundle.getInt("worldSize"))
        mapView.worldMap=generator.worldMap
        mapView.invalidate()

        return root
    }
}