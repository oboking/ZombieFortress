package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.tag
import android.app.FragmentManager
import android.widget.*
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
    private lateinit var viewColonists : View
    private lateinit var viewStorage : View
    private lateinit var viewResearch : View
    private lateinit var viewUpgrades : View
    private lateinit var viewAdmin : View
    private lateinit var viewSettings : View

    private lateinit var fragmentFrame : FrameLayout
    private lateinit var btnTabMenuOverview : ImageButton
    private lateinit var btnTabMenuColonists : ImageButton
    private lateinit var btnTabMenuAdmin : ImageButton
    private lateinit var btnTabMenuStorage : ImageButton
    private lateinit var btnTabMenuResearch : ImageButton
    private lateinit var btnTabMenuUpgrades : ImageButton
    private lateinit var btnTabMenuSettings : ImageButton

    private lateinit var listNewsView: ListView
    private lateinit var listViewColonists: ListView
    private lateinit var listViewStorage : ListView

    private var colonistList : List<Colonist> = listOf(Colonist())

    private var newsList: MutableList<String> = mutableListOf<String>()
    private lateinit var inventory : Inventory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)

        val args = arguments

        var generator : WorldGenerator = WorldGenerator(args.getInt("worldSize"),activity)
        val root = inflater.inflate(R.layout.fragment_game, container, false)

        newsList= mutableListOf()

        //UI
        fragmentFrame = root.findViewById(R.id.fragmentFrame) as FrameLayout
        fragmentFrame.isFocusable=true

        viewOverview = inflater.inflate(R.layout.view_overview, null) as View
        viewColonists = inflater.inflate(R.layout.view_colonists, null) as View
        viewStorage = inflater.inflate(R.layout.view_storage, null) as View
        viewAdmin = inflater.inflate(R.layout.view_admin, null) as View
        viewResearch = inflater.inflate(R.layout.view_research, null) as View
        viewUpgrades = inflater.inflate(R.layout.view_upgrades, null) as View
        viewSettings = inflater.inflate(R.layout.view_settings, null) as View

        listNewsView = viewOverview.findViewById(R.id.listNews) as ListView
        listNewsView.adapter=ArrayAdapter(activity,R.layout.news_list_item,newsList)
        newsList.add(0,"You have started a colony...")
        fragmentFrame.addView(viewOverview)
        inventory= Inventory(this,args.getString("difficulty"))


        listViewColonists = viewColonists.findViewById(R.id.listViewColonists) as ListView
        listViewStorage = viewStorage.findViewById(R.id.listViewStorage) as ListView

        var colonistListAdapter : ColonistListAdapter = ColonistListAdapter(activity,R.layout.colonist_list_item,colonistList)
        listViewColonists.adapter=colonistListAdapter
        var storageListAdapter : StorageListAdapter = StorageListAdapter(activity,R.layout.list_item_storage,inventory.getInventory())
        listViewStorage.adapter=storageListAdapter

        val mapView = root.findViewById(R.id.mapView) as MapView
        textClock = root.findViewById(R.id.textClock) as TextView
        textDate = root.findViewById(R.id.textDate) as TextView
        btnTabMenuOverview = root.findViewById(R.id.btnTabMenuOverview) as ImageButton
        btnTabMenuColonists = root.findViewById(R.id.btnTabMenuColonists) as ImageButton
        btnTabMenuStorage = root.findViewById(R.id.btnTabMenuStorage) as ImageButton
        btnTabMenuAdmin = root.findViewById(R.id.btnTabMenuAdmin) as ImageButton
        btnTabMenuResearch = root.findViewById(R.id.btnTabMenuResearch) as ImageButton
        btnTabMenuUpgrades = root.findViewById(R.id.btnTabMenuUpgrades) as ImageButton
        btnTabMenuSettings = root.findViewById(R.id.btnTabMenuSettings) as ImageButton


        //Generate the world
        generator.init()
        generator.placeRandomTiles()
        generator.generateLandmass()
        generator.randomizeBiomes()

        mapView.mapTiles=generator.mapTiles
        mapView.updateMapSize(args.getInt("worldSize"))
        mapView.worldMap=generator.worldMap
        mapView.invalidate()



        (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        listNewsView.smoothScrollToPosition(0)


        mapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val fragmentManager = fragmentManager
                val fragment = FragmentMap()
                val bundle = Bundle()
                bundle.putSerializable("generator", generator)
                bundle.putInt("worldSize",args.getInt("worldSize"))
                fragment.arguments = bundle
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment as Fragment, "MAP_FRAGMENT").commit()
                return true
            }
        })

        btnTabMenuColonists.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewColonists)
        }
        btnTabMenuOverview.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewOverview)
        }
        btnTabMenuStorage.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewStorage)
        }
        btnTabMenuUpgrades.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewUpgrades)
        }
        btnTabMenuResearch.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewResearch)
        }
        btnTabMenuAdmin.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewAdmin)
        }
        btnTabMenuSettings.setOnClickListener {
            fragmentFrame.removeAllViews()
            fragmentFrame.addView(viewSettings)
        }


        updateResourceTexts(root)

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
    fun updateResourceTexts(rootview: View){
        (rootview.findViewById(R.id.woodAmountText) as TextView).text=(inventory.getAmountOfItem("Wood")).toString()
        (rootview.findViewById(R.id.greensAmountText) as TextView).text=(inventory.getAmountOfItem("Greens")).toString()
        (rootview.findViewById(R.id.meatAmountText) as TextView).text=(inventory.getAmountOfItem("Meat")).toString()
        (rootview.findViewById(R.id.metalAmountText) as TextView).text=(inventory.getAmountOfItem("Metal")).toString()
        (rootview.findViewById(R.id.medpackAmountText) as TextView).text=(inventory.getAmountOfItem("Medpack")).toString()

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
        }else{
            stringClockoutputMinutes = currentMinutes.toString()
        }
        if(Math.log10(currentHour.toDouble()).toInt() + 1==1) {
            stringClockOutputHours = "0" + currentHour.toString()
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

        if(stringClockoutputMinutes.equals("0")){
            stringClockoutputMinutes="00"
        }
        if(stringClockOutputHours.equals("0")){
            stringClockOutputHours="00"
        }
        activity.runOnUiThread({
            textDate.text= stringDateOutputYear + "-" +stringDateOutputMonth+ "-" + stringDateOutputDay
            textClock.text= stringClockOutputHours+ ":" +stringClockoutputMinutes
        })



    }

}


