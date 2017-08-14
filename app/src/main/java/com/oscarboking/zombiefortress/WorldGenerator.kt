package com.oscarboking.zombiefortress

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.util.Random
import android.graphics.Color
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by boking on 2017-06-10.
 */
class WorldGenerator(val size: Int, val context: Activity) : Serializable{

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
        mapTiles = array2d<Tile>(size,size){Tile("blank",(100) * r.nextDouble())}
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
                        mapTiles!![row][cell].type="land"

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
        var z : Int = 0
        var x : Int = 0
        var y : Int = 0
        while (z<=20){

            var nextBiome : Int = Random().nextInt(4)//0=forest, 1=plains, 2=snow, 3=desert, 4=water
            x = Random().nextInt(size)
            y = Random().nextInt(size)
            while((isLand(x,y))==0){
                x = Random().nextInt(size)
                y = Random().nextInt(size)
            }
            if(nextBiome==0){//forest
                mapTiles!![x][y].type="forest"
            }else if (nextBiome==1){
                mapTiles!![x][y].type="plains"
            }else if (nextBiome==2){
                mapTiles!![x][y].type="snow"
            }else if (nextBiome==3){
                mapTiles!![x][y].type="desert"
            }else if (nextBiome==4){
                mapTiles!![x][y].type="water"
            }

            z++
        }

        //now spread biomes
        //ta en snapshot av hur mapen ser ut, gp inom O(n) och vid de som har en typ förutom land, sätt de runtom till samma typ
        /*var mapTilesSnapshot: Array<Array<Tile>>? = mapTiles

        while(stillBlankTiles()){
            for (row in 0..size - 1) {
                for (cell in 0..size - 1) {
                    if(!(mapTilesSnapshot!![row][cell].type.equals("land"))&&!(mapTilesSnapshot!![row][cell].type.equals("water"))){
                        mapTiles!![row+1][cell].type=mapTilesSnapshot!![row][cell].type
                        mapTiles!![row-1][cell].type=mapTilesSnapshot!![row][cell].type
                        mapTiles!![row][cell-1].type=mapTilesSnapshot!![row][cell].type
                        mapTiles!![row][cell+1].type=mapTilesSnapshot!![row][cell].type
                    }
                }
            }
        }*/
    }

    fun stillBlankTiles(): Boolean{
        for (row in 0..size - 1) {
            for (cell in 0..size - 1) {
                if(mapTiles!![row][cell].type.equals("land")){
                    return true
                }
            }
        }
        return false
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