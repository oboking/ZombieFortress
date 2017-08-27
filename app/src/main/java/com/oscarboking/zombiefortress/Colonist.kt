package com.oscarboking.zombiefortress

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by boking on 2017-08-15.
 */
class Colonist : Serializable{
    private var name : String
    private var maleNames : ArrayList<String>
    private var femaleNames : ArrayList<String>
    private var chosenNameList : ArrayList<String> = arrayListOf()
    private var currentJob : String = "nothing" //can be, nothing, supplyrun, hunting, farming, woodchopping

    init {
        maleNames = arrayListOf("Tom", "David", "George", "Peter", "James", "Eric", "Alan", "Gary", "Ben", "Bob", "Max", "Marty", "John", "Malcolm")
        femaleNames = arrayListOf("Sofia", "Clair", "Abby", "Beth", "Emma", "Holly", "Kate", "Kim", "Marie", "Sarah", "Theresa", "Tori", "Victoria", "Wendy")
        name=getRandomName()
    }
    fun getRandomName() : String{

        if(Random().nextInt(2)==1){//male
            chosenNameList = maleNames
        }else{//female
            chosenNameList = femaleNames
        }

        return chosenNameList[Random().nextInt(chosenNameList.size)]
    }
    fun getName() : String{
        return name
    }
    fun getCurrentJob() : String{
        return currentJob
    }
    fun setJob(assignment : String){
        currentJob=assignment
    }
}