package com.oscarboking.zombiefortress

/**
 * Created by boking on 2017-08-15.
 */
class Inventory(gameFrag : GameFragment, difficulty : String){

    private var list : MutableList<Item>
    private var difficulty : String
    private var gameFrag : GameFragment
    init {
        list= mutableListOf(Item("sheckels",20))
        this.difficulty=difficulty
        this.gameFrag = gameFrag

        if(difficulty.equals("easy")){
            this.addTo(kotlin.collections.listOf(Item("Greens",50),Item("Meat",30),Item("Wood",75),Item("Medpack",5)))
        }else if (difficulty.equals("normal")){
            this.addTo(kotlin.collections.listOf(Item("Greens",30),Item("Meat",20),Item("Wood",55),Item("Medpack",3)))
        }else if (difficulty.equals("hard")){
            this.addTo(kotlin.collections.listOf(Item("Greens",10),Item("Meat",10),Item("Wood",35),Item("Medpack",1)))
        }
    }

    fun addTo(itemsToAdd : List<Item>){
        for (item in itemsToAdd){
            for(itemInPossession in list){
                if(itemInPossession.getName().equals(item.getName())){ //already in inventory
                    itemInPossession.increaseQuantity(item.getQuantity())
                }
            }
            list.add(item)
        }
        gameFrag.postNews("New items: " + getItemListString(itemsToAdd))
    }

    fun getAmountOfItem(itemName : String) : Int {
        for(item in list){
            if(item.getName().equals(itemName)){
                return item.getQuantity()
            }
        }
        return 0
    }

    fun getItemListString(itemList : List<Item>):String{

        var listString : String = ""

        for(item in itemList){
            listString += item.getName() + " x" + item.getQuantity() + ". "
        }
        return  listString
    }

    override fun toString() : String{
        return getItemListString(list)
    }

    fun getInventory(): MutableList<Item>{
        return list
    }
}