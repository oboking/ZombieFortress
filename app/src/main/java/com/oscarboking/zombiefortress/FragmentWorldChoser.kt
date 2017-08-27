package com.oscarboking.zombiefortress

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.content.Context.MODE_PRIVATE
import java.io.*


/**
 * Created by boking on 2017-08-12.
 */

class FragmentWorldChoser : Fragment() {

    private lateinit var worldListView : ListView
    private var saveStateList: MutableList<SaveState> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragmentworldchoser, container, false)
        val buttonNewWorld : Button = root.findViewById(R.id.buttonNewWorld) as Button
        worldListView = root.findViewById(R.id.worldListView) as ListView
        worldListView.adapter=ArrayAdapter(activity,R.layout.list_item_world,saveStateList)

        buttonNewWorld.setOnClickListener {
            val fragmentManager = fragmentManager
            val fragment = FragmentCreateWorld()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "WORLDCREATE_FRAGMENT").commit()
        }

        val fis: FileInputStream
        var ois: ObjectInputStream? = null

        try {
            println("loading saves")
            fis = activity.applicationContext.openFileInput("swagsaves")
            ois = ObjectInputStream(fis)
            var allSaveStates :AllSaveStates = (ois.readObject()) as AllSaveStates

            saveStateList=allSaveStates.saveStateList
            ois.close()
        } catch (e: Exception) {
            println("error loading saves")
        } finally {
            if (ois != null)
                try {
                    ois.close()
                } catch (e: Exception) {
                    println("error closing input stream")
                }
        }

        /*
        try {
            println("loading saves")
            val fis = activity.openFileInput("allSaveStates.data")
            val `is` = ObjectInputStream(fis)

            val allSaveStates = (`is`.readObject()) as MutableList<SaveState>

            saveStateList=allSaveStates
            println("savestatelist.length: " + saveStateList.size)
            `is`.close()
            fis.close()
        }catch  (e1: IOException){
            println("error loading saves")
            e1.printStackTrace()
        }*/

        (worldListView.adapter as ArrayAdapter<WorldListAdapter>).notifyDataSetChanged()


        return root
    }
}