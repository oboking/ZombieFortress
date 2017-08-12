package com.oscarboking.zombiefortress

import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.util.Random
import android.graphics.Color

/**
 * Created by boking on 2017-06-10.
 */
class WorldGenerator(val size: Int, val context: AppCompatActivity) {

    var worldMap: Array<IntArray>? = null
    var newWorldMap: Array<IntArray>? = null
    var randomMap: Array<IntArray>? = null
    var mapTiles: Array<Array<Tile>>? = null
    var prevTemp: Int = Random().nextInt(20)
    var prevHumidity: Int = Random().nextInt(50)
    var landRatio: Int = 1
    val r = Random()

    fun init(){
        worldMap = Array(size) { IntArray(size) }
        newWorldMap = Array(size) {IntArray(size)}
        mapTiles = array2d<Tile>(size,size){Tile("water",(100) * r.nextDouble())}
    }

    public inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int)->INNER): Array<Array<INNER>>
            = Array(sizeOuter) { Array<INNER>(sizeInner, innerInit) }

    fun placeRandomTiles(){
        for (rowNum in 0..size-1){
            for(cellNum in 0..size-1){
                //row[cell] = Math.floor(Math.random() * (1 + 1)).toInt()
                worldMap!![rowNum][cellNum] = Random().nextInt(2);
                println("setting row: " + rowNum + ", cell: " + cellNum + ", to: " + worldMap!![rowNum][cellNum])
            }
        }
        randomMap=worldMap
    }

    fun generateLandmass(){
        for(i in 1..landRatio+1) {
            for (row in 0..size - 1) {
                for (cell in 0..size - 1) {
                    if (isLand(cell, row) == 1 && isLand(cell - 1, row - 1) + isLand(cell, row - 1) + isLand(cell + 1, row - 1) +
                            isLand(cell - 1, row) + isLand(cell + 1, row) +
                            isLand(cell - 1, row + 1) + isLand(cell, row + 1) + isLand(cell + 1, row + 1) >= 4
                            || isLand(cell, row) == 0 && isLand(cell - 1, row - 1) + isLand(cell, row - 1) + isLand(cell + 1, row - 1) +
                            isLand(cell - 1, row) + isLand(cell + 1, row) +
                            isLand(cell - 1, row + 1) + isLand(cell, row + 1) + isLand(cell + 1, row + 1) >= 5) {
                        if(i==landRatio+1){ //last iteration

                            println("last iteration")
                        }
                        newWorldMap!![cell][row] = 1
                        mapTiles!![row][cell].type="forest"

                    } else {
                        newWorldMap!![cell][row] = 0;
                        mapTiles!![row][cell].type="water"
                        mapTiles!![row][cell].soilQuality=0.0
                    }
                }
            }
            printWorldMap()
            worldMap = newWorldMap;
            newWorldMap = Array(size) { IntArray(size) }
        }
    }

    fun randomizeBiomes(){
        for (row in 0..size - 1) {
            for (cell in 0..size - 1) {
                if(mapTiles!![cell][row]!=null&& worldMap!![cell][row]!=0) {
                    /*var rand = Random().nextInt(4)
                    if (rand == 0) {//forest
                        mapTiles!![cell][row].type = "forest"
                    } else if (rand == 1) { //plains
                        mapTiles!![cell][row].type = "plains"
                    } else if (rand == 2) { //snow
                        mapTiles!![cell][row].type = "snow"
                    } else if (rand == 3) { //desert
                        mapTiles!![cell][row].type = "desert"
                    }*/
                }
            }
        }
        /* TODO: even the biomes out
        for (row in 0..size - 1) {
            for (cell in 0..size - 1) {
                var nbrOfSameTypeNeighbours: Int = 0

                if(mapTiles!![cell][row]!=null) {
                    if (isSameType(cell,row,cell-1,row-1)){


                        newWorldMap!![cell][row] = 1;

                    }
                }
            }
        }
        */
    }

    fun isSameType(x1: Int, y1: Int, x2: Int, y2: Int): Boolean{
        if(mapTiles!![x1][y1].type.equals(mapTiles!![x2][y2].type)){
            return true
        }else{
            return false
        }
    }

    fun updateLandRatio(ratio: Int){
        landRatio=ratio
        worldMap=randomMap
        generateLandmass()
    }
    //Returns 1 if land
    fun isLand(x: Int, y: Int) : Int{
        if(x <0 ||y<0||x>size-1||y>size-1) {
            return 0;
        }else{
            return worldMap!![x][y];
        }
    }

    fun printWorldMap(){
        for (row in 0..size - 1) {
            for (cell in 0..size - 1) {
                if(worldMap!![row][cell]==1){
                    print("X")
                }else{
                    print(" ")
                }
            }
            println("")
        }
    }

}