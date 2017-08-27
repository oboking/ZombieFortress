package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.view.*
import android.widget.*
import java.io.*
import android.widget.TextView




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
    private lateinit var viewMap : View

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
    private lateinit var mapView : MapView
    private lateinit var bigMapView : MapView
    private lateinit var mapViewTypeText : TextView
    private lateinit var mapViewScoutButton : Button
    private lateinit var mapViewAttackButton : Button
    private lateinit var mapViewPosText : TextView

    private lateinit var pauseButton : Button

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
    private var housing : Int = 10

    private lateinit var gameView : View
    private lateinit var generator : WorldGenerator

    private var saveState : SaveState = SaveState()
    private var saveStateList : MutableList<SaveState> = mutableListOf()

    private var paused :Boolean=false
    private var showingMap :Boolean=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)

        val args = arguments

        generator = WorldGenerator(args.getInt("worldSize"),activity)
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
        viewMap = inflater.inflate(R.layout.view_map,null) as View


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

        mapView = root.findViewById(R.id.mapView) as MapView
        bigMapView = viewMap.findViewById(R.id.viewMapView) as MapView
        mapViewTypeText = viewMap.findViewById(R.id.mapViewTypeText) as TextView
        mapViewScoutButton = viewMap.findViewById(R.id.mapViewScoutButton) as Button
        mapViewAttackButton = viewMap.findViewById(R.id.mapViewAttackButton) as Button
        mapViewPosText = viewMap.findViewById(R.id.mapViewPosText) as TextView

        textClock = root.findViewById(R.id.textClock) as TextView
        textDate = root.findViewById(R.id.textDate) as TextView
        btnTabMenuOverview = root.findViewById(R.id.btnTabMenuOverview) as ImageButton
        btnTabMenuColonists = root.findViewById(R.id.btnTabMenuColonists) as ImageButton
        btnTabMenuStorage = root.findViewById(R.id.btnTabMenuStorage) as ImageButton
        btnTabMenuAdmin = root.findViewById(R.id.btnTabMenuAdmin) as ImageButton
        btnTabMenuResearch = root.findViewById(R.id.btnTabMenuResearch) as ImageButton
        btnTabMenuUpgrades = root.findViewById(R.id.btnTabMenuUpgrades) as ImageButton
        btnTabMenuSettings = root.findViewById(R.id.btnTabMenuSettings) as ImageButton

        pauseButton = root.findViewById(R.id.pauseButton) as Button

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
                unAssignColonist("Farming")
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
                assignAvailableColonist("Farming")
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
                unAssignColonist("Hunting")
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
                assignAvailableColonist("Hunting")
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
                unAssignColonist("WoodCutting")
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
                assignAvailableColonist("WoodCutting")
            }
        }
        btnDecreaseGuards.setOnClickListener {
            if(currentGuards>0){
                currentGuards--
                availableWorkers++
                textCurrentGuards.text=currentGuards.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                unAssignColonist("Guarding")
            }
        }
        btnIncreaseGuards.setOnClickListener {
            if(availableWorkers>0){
                currentGuards++
                availableWorkers--
                textCurrentGuards.text=currentGuards.toString()
                textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
                assignAvailableColonist("Guarding")
            }
        }

        pauseButton.setOnClickListener {
            if(paused){
                paused=false
                pauseButton.text="Pause"
            }else{
                paused=true
                pauseButton.text="Resume"
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
        bigMapView.mapTiles=generator.mapTiles
        bigMapView.updateMapSize(args.getInt("worldSize"))
        bigMapView.worldMap=generator.worldMap


        (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        listNewsView.smoothScrollToPosition(0)



        mapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    if (showingMap) {
                        showingMap = false
                        fragmentFrame.removeAllViews()
                        fragmentFrame.addView(viewOverview)
                    } else {
                        showingMap = true
                        fragmentFrame.removeAllViews()
                        fragmentFrame.addView(viewMap)
                    }
                    bigMapView.invalidate()
                }
                return true
            }
        })


        bigMapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    var selectedX=(generator.size*(event.x/bigMapView.width)).toInt()
                    var selectedY=(generator.size*(event.y/bigMapView.height)).toInt()
                    println("touch! x: " + event.x + ", y: " + event.y + ", event/mapview.width: "+ (event.x/mapView.width) + ", selectedX: " + selectedX )
                    bigMapView.selectedX = selectedX
                    bigMapView.selectedY = selectedY
                    bigMapView.invalidate()
                    mapViewTypeText.text= "Type: " + mapView.mapTiles!![selectedY][selectedX].type.substring(0,1).toUpperCase() + mapView.mapTiles!![selectedY][selectedX].type.substring(1)
                    mapViewPosText.text="X: " + selectedX + " Y: " + selectedY
                    if( mapView.mapTiles!![selectedY][selectedX].type.equals("colony")){
                        mapViewAttackButton.visibility=View.VISIBLE
                    }else{
                        mapViewAttackButton.visibility=View.GONE
                    }
                    if( mapView.mapTiles!![selectedY][selectedX].type.equals("home")) {
                        mapViewAttackButton.visibility = View.GONE
                        mapViewScoutButton.visibility = View.GONE
                    }else{
                        mapViewScoutButton.visibility = View.VISIBLE
                    }
                }
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
            addColonists(10)
        }else if(args.getString("difficulty").equals("normal")){
            addColonists(5)
        }else if(args.getString("difficulty").equals("hard")){
            addColonists(3)
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

        setRandomLandTileType("infected")
        setRandomLandTileType("home")

        mapView.invalidate()
        bigMapView.invalidate()
        saveGame()
        tick()
        return root
    }

    fun addColonists(nbrOfColonists : Int){
        var x = 0
        var colonistName = ""

        while(x<nbrOfColonists && housing>colonistList.size){
            var colonist = Colonist()
            colonistList.add(colonist)
            postNews("New colonist: " + colonist.getName())
            x++
        }

        (listViewColonists.adapter as ArrayAdapter<ColonistListAdapter>).notifyDataSetChanged()

        availableWorkers+=x
        textAvailableWorkers.text="Available workers: " +availableWorkers.toString()
    }

    fun updateResourceTexts(rootview: View){
        (rootview.findViewById(R.id.woodAmountText) as TextView).text=(inventory.getAmountOfItem("Wood")).toString()
        (rootview.findViewById(R.id.greensAmountText) as TextView).text=(inventory.getAmountOfItem("Greens")).toString()
        (rootview.findViewById(R.id.meatAmountText) as TextView).text=(inventory.getAmountOfItem("Meat")).toString()
        (rootview.findViewById(R.id.metalAmountText) as TextView).text=(inventory.getAmountOfItem("Metal")).toString()
        (rootview.findViewById(R.id.medpackAmountText) as TextView).text=(inventory.getAmountOfItem("Medpack")).toString()
        (rootview.findViewById(R.id.housingAmountText) as TextView).text=colonistList.size.toString() + "/" + housing.toString()
        (rootview.findViewById(R.id.meleeAmountText) as TextView).text=inventory.getAmountOfItem("meleeWeapon").toString() + "/" + colonistList.size
        (rootview.findViewById(R.id.rangeAmountText) as TextView).text=inventory.getAmountOfItem("rangedWeapon").toString() + "/" + colonistList.size
        if(colonistList.size==housing){
            (rootview.findViewById(R.id.housingAmountText) as TextView).setTextColor(Color.RED)
        }else{
            (rootview.findViewById(R.id.housingAmountText) as TextView).setTextColor(Color.WHITE)
        }
        if(inventory.getAmountOfItem("meleeWeapon")>=colonistList.size){
            (rootview.findViewById(R.id.meleeAmountText) as TextView).setTextColor(Color.GREEN)
        }else{
            (rootview.findViewById(R.id.meleeAmountText) as TextView).setTextColor(Color.WHITE)
        }
        if(inventory.getAmountOfItem("rangedWeapon")>=colonistList.size){
            (rootview.findViewById(R.id.rangeAmountText) as TextView).setTextColor(Color.GREEN)
        }else{
            (rootview.findViewById(R.id.rangeAmountText) as TextView).setTextColor(Color.WHITE)
        }


    }

    fun postNews(news : String){
        activity.runOnUiThread(Runnable {
            newsList.add(0, news)
            (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            listNewsView.smoothScrollToPosition(0)
        })

    }

    fun tick(){
        if(!paused) {
            if (currentMinutes == 59) {
                currentMinutes = 0
                inventory.addTo(listOf(Item("Greens", greensPerDay), Item("Meat", meatPerDay), Item("Wood", woodPerDay)))

                //Every hour
                consumeFood()

                if (currentHour == 23) {
                    currentHour = 0
                    if (((currentMonth == 1 || currentMonth == 3 || currentMonth == 5 || currentMonth == 7 || currentMonth == 8 || currentMonth == 10 || currentMonth == 12) && currentDay == 31) || ((currentMonth == 4 || currentMonth == 6 || currentMonth == 9 || currentMonth == 11) && currentDay == 30) || ((currentMonth == 2) && currentDay == 28)) {
                        currentDay = 1
                        if (currentMonth == 12) {
                            currentMonth = 1
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    } else {
                        currentDay++
                    }

                } else {
                    currentHour++
                }
            } else {
                //every second run event
                randomizeEvent()
                activity.runOnUiThread({
                    updateResourceTexts(gameView)
                    mapView.invalidate()
                    bigMapView.invalidate()
                })
                currentMinutes++
            }
            if (Math.log10(currentMinutes.toDouble()).toInt() + 1 == 1) {
                stringClockoutputMinutes = "0" + currentMinutes.toString()
            } else {
                stringClockoutputMinutes = currentMinutes.toString()
            }
            if (Math.log10(currentHour.toDouble()).toInt() + 1 == 1) {
                stringClockOutputHours = "0" + currentHour.toString()
            } else {
                stringClockOutputHours = currentHour.toString()
            }
            if (Math.log10(currentDay.toDouble()).toInt() + 1 == 1) {
                stringDateOutputDay = "0" + currentDay.toString()
            } else {
                stringDateOutputDay = currentDay.toString()
            }
            if (Math.log10(currentMonth.toDouble()).toInt() + 1 == 1) {
                stringDateOutputMonth = "0" + currentMonth.toString()
            } else {
                stringDateOutputMonth = currentMonth.toString()
            }

            if (stringClockoutputMinutes.equals("0")) {
                stringClockoutputMinutes = "00"
            }
            if (stringClockOutputHours.equals("0")) {
                stringClockOutputHours = "00"
            }
            activity.runOnUiThread({
                textDate.text = stringDateOutputYear + "-" + stringDateOutputMonth + "-" + stringDateOutputDay
                textClock.text = stringClockOutputHours + ":" + stringClockoutputMinutes
            })

        }

    }

    fun saveGame(){
        print("saving game")
        saveState.colonistList=colonistList
        saveState.newsList=newsList
        saveState.currentDay=currentDay
        saveState.startDay=startDay
        saveState.name="mr save"
        saveState.generator=generator

        saveStateList.add(saveState)

        val fos: FileOutputStream
        var oos: ObjectOutputStream? = null
        var allSaveStates :AllSaveStates = AllSaveStates()
        allSaveStates.saveStateList=saveStateList
        try {
            fos = activity.applicationContext.openFileOutput("swagsaves", Context.MODE_PRIVATE)
            oos = ObjectOutputStream(fos)
            oos.writeObject(allSaveStates)
            oos.close()
        } catch (e: Exception) {
        } finally {
            if (oos != null)
                try {
                    oos.close()
                } catch (e: Exception) {
                }
        }


        //AllSaveStates.saveStateList.add(saveState)
        /*
        try{

            val fos = activity.openFileOutput("allSaveStates.data", Context.MODE_PRIVATE)
            val os = ObjectOutputStream(fos)
            //println("AllSaveStates.list.length: " + AllSaveStates.saveStateList.size)
            os.writeObject(saveStateList)
            os.close()
            fos.close()
        } catch  (e1: IOException){
            e1.printStackTrace()
        }*/

        println("saved game")
    }

    fun assignAvailableColonist(assignment : String){
        for(colonist in colonistList){
            if(colonist.getCurrentJob().equals("nothing") && !colonist.getCurrentJob().equals("Sick")){
                colonist.setJob(assignment)
                return
            }
        }
    }

    fun assignAnyColonist(assignment : String){
        for(colonist in colonistList){
            if(!colonist.getCurrentJob().equals(assignment)) {
                colonist.setJob(assignment)
                if(assignment.equals("Sick")){
                    postNews(colonist.getName() + " has gotten sick!")
                }
                return
            }
        }

    }

    fun unAssignColonist(assignment : String){
        for(colonist in colonistList){
            if(colonist.getCurrentJob().equals(assignment)){
                colonist.setJob("nothing")
                return
            }
        }
    }

    fun setRandomLandTileType(type : String){
        var foundLand : Boolean = false
        var x= 0
        var y= 0
        while(!foundLand) {
            x = Random().nextInt(mapView.mapSize - 1)
            y = Random().nextInt(mapView.mapSize - 1)
            if (generator.isLand(y, x) == 1 && mapView.mapTiles!![x][y].type!="home") {
                foundLand = true
            }
        }
        mapView.mapTiles!![x][y].type = type
        bigMapView.mapTiles!![x][y].type = type
        generator.mapTiles!![x][y].type= type
    }

    fun randomizeEvent(){

        var random = Random().nextInt(35)
        if(random==1){
            //colonists gets sick
            //assignAnyColonist("Sick")
        }else if (random==2){
            //radiotransmission from another colony reveals them on map, either they try to find and you answer or they broadcast their location. player chooses what to do
            //dsplaya custom dialog with choice
            showInfoDialog("Incoming radio transmission!","We've picked up a radio signal telling us the location of another colony. The location has been marked on the map!")
            setRandomLandTileType("colony")
        }else if (random==3){
            //one/more ppl arrive in search of shelter. you can take them in or let them go
            showDialogIntake()
        }else if (random==4){
            //one/more ppl attack your colony
        }else if (random==5){
            //one/more zombies attach the colony
        }else if (random==6){
            spreadVirus()
        }else if (random==7){
            //lumberjack gets killed outside of the colony
        }else if (random==8){
            //random land tile is infected.
            setRandomLandTileType("infected")
        }else if (random==9){

        }else if (random==10){

        }else if (random==11){

        }else if (random==12){

        }else if (random==13){

        }else if (random==14){

        }else if (random==15){

        }else if (random==16){

        }
    }

    fun showInfoDialog(title: String, text : String){

        activity.runOnUiThread(Runnable {
            paused=true

            var dialog : Dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_information)

            val titleTextView = dialog.findViewById(R.id.dialogInformationTitleText) as TextView
            val bodyTextView = dialog.findViewById(R.id.dialogInformationBodyText) as TextView
            val button = dialog.findViewById(R.id.dialogInformationButton) as Button

            titleTextView.text = title
            bodyTextView.text = text

            button.setOnClickListener {
                paused=false
                dialog.dismiss()
            }

            dialog.show()

        })

    }

    fun showDialogIntake(){

        activity.runOnUiThread(Runnable {
            paused=true

            var dialog : Dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_intake)

            val nbrOfPeople = Random().nextInt(3)+1

            val bodyTextView = dialog.findViewById(R.id.dialogIntakeBodyText) as TextView
            val negButton = dialog.findViewById(R.id.dialogIntakeNegative) as Button
            val posButton = dialog.findViewById(R.id.dialogIntakeButtonPositive) as Button

            bodyTextView.text = nbrOfPeople.toString() + " people have turned up at your gate looking for shelter. They can help our colony expand but also need food and housing. Are these people to be trusted?"

            negButton.setOnClickListener {
                paused=false
                dialog.dismiss()
            }

            posButton.setOnClickListener {
                addColonists(nbrOfPeople)
                paused=false
                dialog.dismiss()
            }

            dialog.show()

        })

    }

    fun spreadVirus(){
        var currentMapTiles: Array<Array<Tile>>? = mapView.mapTiles
        for (j in 0..mapView.mapSize - 1) {
            for (i in 0..mapView.mapSize - 1) {
                if (currentMapTiles!![i][j] != null) {
                    if (currentMapTiles!![i][j].type.equals("infected")) {
                            var random = Random().nextInt(8)
                            if (random == 0) {
                                if(i+1<=mapView.mapSize-1) {
                                    if (currentMapTiles!![i + 1][j].type != "water" || currentMapTiles!![i + 1][j].type != "home" || currentMapTiles!![i + 1][j].type != "colony") {
                                        if(!mapView.mapTiles!![i + 1][j].type.equals("water") || !mapView.mapTiles!![i + 1][j].type.equals("home") || !mapView.mapTiles!![i + 1][j].type.equals("colony")) {
                                            mapView.mapTiles!![i + 1][j].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 1) {
                                if(i-1>=0) {
                                    if (currentMapTiles!![i - 1][j].type != "water" || currentMapTiles!![i - 1][j].type != "home" || currentMapTiles!![i - 1][j].type != "colony") {
                                        if(!mapView.mapTiles!![i - 1][j].type.equals("water") || !mapView.mapTiles!![i - 1][j].type.equals("home") || !mapView.mapTiles!![i - 1][j].type.equals("colony")){
                                            mapView.mapTiles!![i - 1][j].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 2) {
                                if(j+1<=mapView.mapSize-1) {
                                    if (currentMapTiles!![i][j + 1].type != "water" || currentMapTiles!![i][j + 1].type != "home" || currentMapTiles!![i][j + 1].type != "colony") {
                                        if(!mapView.mapTiles!![i][j + 1].type.equals("water")|| !mapView.mapTiles!![i][j + 1].type.equals("home")||!mapView.mapTiles!![i][j + 1].type.equals("colony")) {
                                            mapView.mapTiles!![i][j + 1].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 3) {
                                if(j-1>=0) {
                                    if (currentMapTiles!![i][j - 1].type != "water" || currentMapTiles!![i][j - 1].type != "home" || currentMapTiles!![i][j - 1].type != "colony") {
                                        if(!mapView.mapTiles!![i][j - 1].type.equals("water")||!!mapView.mapTiles!![i][j - 1].type.equals("home") ||!mapView.mapTiles!![i][j - 1].type.equals("colony")) {
                                            mapView.mapTiles!![i][j - 1].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 4) {
                                if(j+1<=mapView.mapSize-1 && i+1<=mapView.mapSize-1) {
                                    if (currentMapTiles!![i + 1][j + 1].type != "water" || currentMapTiles!![i + 1][j + 1].type != "home" || currentMapTiles!![i + 1][j + 1].type != "colony") {
                                        if(!mapView.mapTiles!![i+1][j + 1].type.equals("water")||!mapView.mapTiles!![i+1][j + 1].type.equals("home")||!mapView.mapTiles!![i+1][j + 1].type.equals("colony")) {
                                            mapView.mapTiles!![i + 1][j + 1].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 5) {
                                if(j-1>=0&&i-1>=0) {
                                    if (currentMapTiles!![i - 1][j - 1].type != "water" || currentMapTiles!![i - 1][j - 1].type != "home" || currentMapTiles!![i - 1][j - 1].type != "colony") {
                                        if(!mapView.mapTiles!![i-1][j - 1].type.equals("water")||!mapView.mapTiles!![i-1][j - 1].type.equals("home")||!mapView.mapTiles!![i-1][j - 1].type.equals("colony")) {
                                            mapView.mapTiles!![i - 1][j - 1].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 6) {
                                if(j-1>=0&&i+1<=mapView.mapSize-1) {
                                    if (currentMapTiles!![i + 1][j - 1].type != "water" || currentMapTiles!![i + 1][j - 1].type != "home" || currentMapTiles!![i + 1][j - 1].type != "colony") {
                                        if(!mapView.mapTiles!![i+1][j - 1].type.equals("water")||!mapView.mapTiles!![i+1][j - 1].type.equals("home")||!mapView.mapTiles!![i+1][j - 1].type.equals("colony")) {
                                            mapView.mapTiles!![i + 1][j - 1].type = "infected"
                                        }
                                    }
                                }
                            } else if (random == 7) {
                                if(i-1>=0&&j+1<=mapView.mapSize-1) {
                                if (currentMapTiles!![i - 1][j + 1].type != "water" || currentMapTiles!![i - 1][j + 1].type != "home" || currentMapTiles!![i - 1][j + 1].type != "colony") {
                                    if(!mapView.mapTiles!![i-1][j + 1].type.equals("water")||!mapView.mapTiles!![i-1][j + 1].type.equals("home")||!mapView.mapTiles!![i-1][j + 1].type.equals("colony")) {
                                        mapView.mapTiles!![i - 1][j + 1].type = "infected"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun consumeFood(){
        inventory.decreaseQuantityofItem("Greens",colonistList.size)
        inventory.decreaseQuantityofItem("Meat",colonistList.size)
        activity.runOnUiThread(Runnable {
            updateResourceTexts(gameView)
        })
    }
}


