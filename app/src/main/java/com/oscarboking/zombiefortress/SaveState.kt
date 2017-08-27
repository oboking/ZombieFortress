package com.oscarboking.zombiefortress

import java.io.*

/**
 * Created by boking on 2017-08-26.
 */

class SaveState : Serializable{
    var name : String = ""
    var mapSize : Int = 0
    var startDay : Int = 0
    var startMonth : Int = 0
    var startYear : Int = 0

    var currentMinutes : Int = 0
    var currentHour : Int = 0
    var currentDay : Int = 0
    var currentMonth : Int = 0
    var currentYear : Int = 0


    var colonistList : MutableList<Colonist> = mutableListOf()
    var newsList: MutableList<String> = mutableListOf<String>()

    var itemList : MutableList<Item> = mutableListOf()

    lateinit var generator : WorldGenerator

}