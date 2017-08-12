package com.oscarboking.zombiefortress

import java.util.*

/**
 * Created by boking on 2017-06-13.
 */
class Tile(type:String,soilQuailty:Double) {
    val r = Random()
    var temp: Int = 20
    var soilQuality: Double = soilQuailty
    var humidity: Int = 1
    var type: String = type //forest, plains, snow, desert, water

    init {

    }
}