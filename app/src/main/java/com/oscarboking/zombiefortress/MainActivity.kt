package com.oscarboking.zombiefortress

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var selectedX : Int = -1
    var selectedY : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonGenerate = findViewById(R.id.buttonGenerate) as Button
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
        mapView.invalidate()


    }
}
