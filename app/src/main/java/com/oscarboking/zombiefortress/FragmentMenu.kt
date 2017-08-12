package com.oscarboking.zombiefortress

import android.os.Bundle
import android.app.Fragment;
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.graphics.Typeface



/**
 * Created by boking on 2017-08-12.
 */
class FragmentMenu : Fragment() {

    var selectedX : Int = -1
    var selectedY : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_mainmenu, container, false)

        val buttonPlay:Button = root.findViewById(R.id.buttonPlay) as Button

        
        buttonPlay.setOnClickListener {
            val fragmentManager = fragmentManager
            val fragment = FragmentWorldChoser()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "WORLDCHOSER_FRAGMENT").commit()

        }
        val tx = root.findViewById(R.id.labelGameName) as TextView

        val custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/DIRTYEGO.TTF")
        tx.typeface = custom_font


        /*val buttonGenerate = findViewById(R.id.buttonGenerate) as Button
        val buttonBiomes = findViewById(R.id.buttonBiomes) as Button
        val mapView = findViewById(R.id.mapView) as MapView
        val mapSizeSeekbar = findViewById(R.id.mapSeekbar) as SeekBar
        val smoothnessSeekbar = findViewById(R.id.smoothnessSeekbar) as SeekBar
        val mapSizeText = findViewById(R.id.mapSizeText) as TextView
        val smoothnessText = findViewById(R.id.smoothnessText) as TextView
        val generator = WorldGenerator(mapSizeSeekbar.progress+10,this)
        val soilQualityText =  findViewById(R.id.soilQuality) as TextView

        mapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    selectedX=(generator.size*(event.x/mapView.width)).toInt()
                    selectedY=(generator.size*(event.y/mapView.height)).toInt()

                    println("touch! x: " + event.x + ", y: " + event.y + ", event/mapview.width: "+ (event.x/mapView.width) + ", selectedX: " + selectedX )
                    mapView.selectedX = selectedX
                    mapView.selectedY = selectedY
                    mapView.invalidate()
                    soilQualityText.text= (generator.mapTiles!![selectedX][selectedY].soilQuality).toInt().toString() + " %"
                }
                return true
            }
        })

        mapSizeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mapSizeText.text= (10+progress).toString()
                mapView.mapSize=mapSizeSeekbar.progress+10
                mapView.updateMapSize(mapSizeSeekbar.progress+10)
                mapView.worldMap=generator.worldMap
                //mapView.landTiles=generator.landTiles
                mapView.invalidate()
            }

        })

        smoothnessSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                smoothnessText.text= progress.toString()
                generator.updateLandRatio(smoothnessSeekbar.progress)
                mapView.worldMap=generator.worldMap
                mapView.invalidate()
            }

        })

        buttonGenerate.setOnClickListener {
            generator.init()
            generator.placeRandomTiles()
            generator.generateLandmass()
            generator.randomizeBiomes()
            mapView.mapTiles=generator.mapTiles
            mapView.updateMapSize(mapSizeSeekbar.progress+10)
            mapView.worldMap=generator.worldMap
            mapView.invalidate()
        }

        buttonBiomes.setOnClickListener {
            generator.randomizeBiomes()
            mapView.mapTiles=generator.mapTiles
            mapView.invalidate()
        }


        //Generate a first random map
        generator.init()
        generator.placeRandomTiles()
        generator.generateLandmass()
        generator.randomizeBiomes()
        mapView.mapTiles=generator.mapTiles
        mapView.updateMapSize(mapSizeSeekbar.progress+10)
        mapView.worldMap=generator.worldMap
        mapView.invalidate()*/



        return root;
    }
}
