package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.tag
import android.app.FragmentManager
import android.widget.FrameLayout
import com.oscarboking.zombiefortress.FragmentOverview.CallBack




/**
 * Created by boking on 2017-08-12.
 */

class GameFragment : Fragment() {


    val tickRate : Int = 1
    var startDay : Int = 0
    var startMonth : Int = 0
    var startYear : Int = 0

    var currentMinutes : Int = 0
    var currentHour : Int = 0
    var currentDay : Int = 0
    var currentMonth : Int = 0
    var currentYear : Int = 0

    var stringClockoutputMinutes: String = ""
    var stringClockOutputHours: String = ""
    var stringDateOutputDay: String = ""
    var stringDateOutputMonth: String = ""
    var stringDateOutputYear: String = ""
    private lateinit var textClock: TextView
    private lateinit var textDate: TextView
    private lateinit var viewOverview : View
    private lateinit var fragmentFrame : FrameLayout

    private lateinit var listNewsView: ListView
    var newsList: MutableList<String> = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)
        var generator : WorldGenerator = WorldGenerator(20,activity)
        val root = inflater.inflate(R.layout.fragment_game, container, false)

        newsList= mutableListOf("ayy","lmao")


        //UI
        fragmentFrame = root.findViewById(R.id.fragmentFrame) as FrameLayout
        fragmentFrame.isFocusable=true
        viewOverview = inflater.inflate(R.layout.view_overview, null) as View
        fragmentFrame.addView(viewOverview)
        listNewsView = viewOverview.findViewById(R.id.listNews) as ListView
        val mapView = root.findViewById(R.id.mapView) as MapView
        textClock = root.findViewById(R.id.textClock) as TextView
        textDate = root.findViewById(R.id.textDate) as TextView


        //Generate the world
        generator.init()
        generator.placeRandomTiles()
        generator.generateLandmass()
        generator.randomizeBiomes()

        mapView.mapTiles=generator.mapTiles
        mapView.updateMapSize(20)
        mapView.worldMap=generator.worldMap
        mapView.invalidate()

        //TRY WITH OVERVIEW View

        listNewsView.adapter=ArrayAdapter(activity,R.layout.news_list_item,newsList)

        newsList.add(0,"You have started a colony...")
        (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        listNewsView.smoothScrollToPosition(0)


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

        //Start world tick
        val t = Timer()
        t.schedule(object : TimerTask() {
            override fun run() {
                tick()
            }
        }, 0, 1000)//1 sec tick

        //Get date and set clock
        val now = Date()
        DateFormat.getTimeInstance(DateFormat.SHORT).format(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val date = sdf.format(now) //15/10/2013
        startDay=(date.substring(8,10)).toInt()
        startMonth=(date.substring(5,7)).toInt()
        startYear=(date.substring(0,4)).toInt()
        currentHour =( date.substring(11,13)).toInt()
        currentMinutes =(date.substring(14,16)).toInt()
        stringDateOutputYear=date.substring(0,4)
        currentMonth=startMonth
        currentDay=startDay

        tick()
        return root
    }

    fun postNews(news : String){
        activity.runOnUiThread(Runnable {
            newsList.add(0, news)
            (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            listNewsView.smoothScrollToPosition(0)
        })

    }

    fun tick(){
        if(currentMinutes==59){
            currentMinutes=0
            if(currentHour==23){
                currentHour=0
                if(((currentMonth==1||currentMonth==3||currentMonth==5||currentMonth==7||currentMonth==8||currentMonth==10||currentMonth==12)&&currentDay==31)||((currentMonth==4||currentMonth==6||currentMonth==9||currentMonth==11)&&currentDay==30)||((currentMonth==2)&&currentDay==28)){
                    currentDay=1
                    if(currentMonth==12){
                        currentMonth=1
                        currentYear++
                    }else{
                        currentMonth++
                    }
                }else{
                    currentDay++
                }

            }else{
                currentHour++
            }
        }else{
            currentMinutes++
        }
        if(Math.log10(currentMinutes.toDouble()).toInt() + 1==1) {
            stringClockoutputMinutes = "0" + currentMinutes.toString()
            if(stringClockoutputMinutes.equals("0 ")){
                stringClockoutputMinutes="00"
            }
        }else{
            stringClockoutputMinutes = currentMinutes.toString()
        }
        if(Math.log10(currentHour.toDouble()).toInt() + 1==1) {
            stringClockOutputHours = "0" + currentHour.toString()
            if(stringClockOutputHours.equals("0 ")){
                stringClockOutputHours="00"
            }
        }else{
            stringClockOutputHours = currentHour.toString()
        }
        if(Math.log10(currentDay.toDouble()).toInt() + 1==1) {
            stringDateOutputDay = "0" + currentDay.toString()
        }else{
            stringDateOutputDay = currentDay.toString()
        }
        if(Math.log10(currentMonth.toDouble()).toInt() + 1==1) {
            stringDateOutputMonth = "0" + currentMonth.toString()
        }else{
            stringDateOutputMonth = currentMonth.toString()
        }

        activity.runOnUiThread({
            textDate.text= stringDateOutputYear + "-" +stringDateOutputMonth+ "-" + stringDateOutputDay
            textClock.text= stringClockOutputHours+ ":" +stringClockoutputMinutes
        })



    }

}


