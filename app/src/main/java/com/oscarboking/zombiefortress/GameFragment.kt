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
import org.w3c.dom.Text


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

    private lateinit var btnDecreaseFarmers : ImageButton
    private lateinit var btnIncreaseFarmers : ImageButton
    private lateinit var btnDecreaseHunters : ImageButton
    private lateinit var btnIncreaseHunters : ImageButton
    private lateinit var btnDecreaseWoodCutters : ImageButton
    private lateinit var btnIncreaseWoodCutters : ImageButton
    private lateinit var btnDecreaseGuards : ImageButton
    private lateinit var btnIncreaseGuards : ImageButton
    private lateinit var textCurrentFarmers : TextView
    private lateinit var textCurrentHunters : TextView
    private lateinit var textCurrentWoodCutters : TextView
    private lateinit var textCurrentGuards : TextView
    private lateinit var textAvailableWorkers : TextView
    private lateinit var textGreensPerDay : TextView
    private lateinit var textMeatPerDay : TextView
    private lateinit var textWoodPerDay : TextView

    private lateinit var listNewsView: ListView
    private lateinit var listViewColonists: ListView
    private lateinit var listViewStorage : ListView

    private var colonistList : MutableList<Colonist> = mutableListOf()

    private var newsList: MutableList<String> = mutableListOf<String>()
    private lateinit var inventory : Inventory

    private var availableWorkers : Int = 0
    private var currentFarmers : Int = 0
    private var currentHunters : Int = 0
    private var currentWoodCutters : Int = 0
    private var currentGuards : Int = 0
    private var greensPerDay : Int = 0
    private var meatPerDay : Int = 0
    private var woodPerDay : Int = 0
    private lateinit var gameView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)

        val args = arguments

        var generator : WorldGenerator = WorldGenerator(args.getInt("worldSize"),activity)
        val root = inflater.inflate(R.layout.fragment_game, container, false)
        gameView=root
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

        btnDecreaseFarmers = viewAdmin.findViewById(R.id.btnDecreaseFarmers) as ImageButton
        btnIncreaseFarmers = viewAdmin.findViewById(R.id.btnIncreaseFarmers) as ImageButton
        btnDecreaseHunters = viewAdmin.findViewById(R.id.btnDecreaseHunters) as ImageButton
        btnIncreaseHunters = viewAdmin.findViewById(R.id.btnIncreaseHunters) as ImageButton
        btnDecreaseWoodCutters = viewAdmin.findViewById(R.id.btnDecreaseWoodCutters) as ImageButton
        btnIncreaseWoodCutters = viewAdmin.findViewById(R.id.btnIncreaseWoodCutters) as ImageButton
        btnDecreaseGuards = viewAdmin.findViewById(R.id.btnDecreaseGuards) as ImageButton
        btnIncreaseGuards = viewAdmin.findViewById(R.id.btnIncreaseGuards) as ImageButton
        textCurrentFarmers = viewAdmin.findViewById(R.id.currentFarmersText) as TextView
        textCurrentHunters = viewAdmin.findViewById(R.id.currentHuntersText) as TextView
        textCurrentWoodCutters = viewAdmin.findViewById(R.id.currentWoodcuttersText) as TextView
        textCurrentGuards = viewAdmin.findViewById(R.id.currentGuardsText) as TextView
        textAvailableWorkers = viewAdmin.findViewById(R.id.availableColonistsText) as TextView
        textGreensPerDay = viewAdmin.findViewById(R.id.textGreensPerDay) as TextView
        textMeatPerDay = viewAdmin.findViewById(R.id.textMeatPerDay) as TextView
        textWoodPerDay = viewAdmin.findViewById(R.id.textWoodPerDay) as TextView
        textAvailableWorkers.text="Available workers: " + availableWorkers.toString()
        btnDecreaseFarmers.setOnClickListener {
            if(currentFarmers>0){
                currentFarmers--
                availableWorkers++
                textCurrentFarmers.text=currentFarmers.toString()
                textAvailableWorkers.text="Available workers: " + availableWorkers.toString()
                textGreensPerDay.text=("Greens per day: " + currentFarmers*9).toString()
                greensPerDay=currentFarmers*9
            }
        }
        btnIncreaseFarmers.setOnClickListener {
            if(availableWorkers>0){
                currentFarmers++
                availableWorkers--
                textCurrentFarmers.text=currentFarmers.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                textGreensPerDay.text=("Greens per day: " +currentFarmers*9).toString()
                greensPerDay=currentFarmers*9
            }
        }
        btnDecreaseHunters.setOnClickListener {
            if(currentHunters>0){
                currentHunters--
                availableWorkers++
                textCurrentHunters.text=currentHunters.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                textMeatPerDay.text=("Meat per day: " +currentHunters*9).toString()
                meatPerDay=currentHunters*9
            }
        }
        btnIncreaseHunters.setOnClickListener {
            if(availableWorkers>0){
                currentHunters++
                availableWorkers--
                textCurrentHunters.text=currentHunters.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                textMeatPerDay.text=("Meat per day: " +currentHunters*9).toString()
                meatPerDay=currentFarmers*9
            }
        }
        btnDecreaseWoodCutters.setOnClickListener {
            if(currentWoodCutters>0){
                currentWoodCutters--
                availableWorkers++
                textCurrentWoodCutters.text=currentWoodCutters.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                textWoodPerDay.text=("Wood per day: " +currentWoodCutters*50).toString()
                woodPerDay=currentWoodCutters*50
            }
        }
        btnIncreaseWoodCutters.setOnClickListener {
            if(availableWorkers>0){
                currentWoodCutters++
                availableWorkers--
                textCurrentWoodCutters.text=currentWoodCutters.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                textWoodPerDay.text=("Wood per day: " +currentWoodCutters*50).toString()
                woodPerDay=currentWoodCutters*50
            }
        }
        btnDecreaseGuards.setOnClickListener {
            if(currentGuards>0){
                currentGuards--
                availableWorkers++
                textCurrentGuards.text=currentGuards.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
            }
        }
        btnIncreaseGuards.setOnClickListener {
            if(availableWorkers>0){
                currentGuards++
                availableWorkers--
                textCurrentGuards.text=currentGuards.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
            }
        }

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
            (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            listNewsView.smoothScrollToPosition(0)

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

        if(args.getString("difficulty").equals("easy")){
            addColonists(5)
        }else if(args.getString("difficulty").equals("normal")){
            addColonists(3)
        }else if(args.getString("difficulty").equals("hard")){
            addColonists(2)
        }

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

    fun addColonists(nbrOfColonists : Int){
        var x = 0
        var colonistName = ""

        while(x<nbrOfColonists){
            var colonist = Colonist()
            colonistList.add(colonist)
            postNews("New colonist: " + colonist.getName())
            x++
        }
        (listViewColonists.adapter as ArrayAdapter<ColonistListAdapter>).notifyDataSetChanged()
        availableWorkers+=nbrOfColonists
        textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
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
            inventory.addTo(listOf(Item("Greens",greensPerDay), Item("Meat", meatPerDay), Item("Wood", woodPerDay)))

            activity.runOnUiThread({
                updateResourceTexts(gameView)
            })


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


