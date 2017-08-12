package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentTabHost
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.R.attr.fragment



/**
 * Created by boking on 2017-08-12.
 */

class GameFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)
        var generator : WorldGenerator = WorldGenerator(20,activity)
        val root = inflater.inflate(R.layout.fragment_game, container, false)
        val mapView = root.findViewById(R.id.mapView) as MapView

        //Generate the world
        generator.init()
        generator.placeRandomTiles()
        generator.generateLandmass()
        generator.randomizeBiomes()

        mapView.mapTiles=generator.mapTiles
        mapView.updateMapSize(20)
        mapView.worldMap=generator.worldMap
        mapView.invalidate()


        mapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val fragmentManager = fragmentManager
                val fragment = FragmentMap()
                val bundle = Bundle()
                bundle.putSerializable("generator", generator)
                fragment.arguments = bundle
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment as Fragment, "MAP_FRAGMENT").commit()
                return true
            }
        })


        return root
    }
}