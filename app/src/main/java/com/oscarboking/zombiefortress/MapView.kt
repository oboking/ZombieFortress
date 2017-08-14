package com.oscarboking.zombiefortress

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.graphics.Canvas
import android.view.View


/**
 * Created by boking on 2017-06-12.
 */
class MapView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,  var mapSize: Int = 1
) : View(context, attrs, defStyleAttr) {

    var paint = Paint()
    var worldMap: Array<IntArray>? = null
    var mapTiles: Array<Array<Tile>>? = null
    var rectSize:Int = 0;
    var selectedX : Int = -1
    var selectedY : Int = -1

    fun updateMapSize(size: Int){
        mapSize=size
        worldMap = Array(mapSize) { IntArray(mapSize) }
    }

    public override fun onDraw(c: Canvas) {
        if (worldMap != null && mapTiles!=null) {
            println("drawing")
            paint.setStrokeWidth(2f);
            rectSize = this.width / mapSize
            println("mapSize: " + mapSize + ", worldmap size: " + worldMap!!.size)
            for (i in 0..mapSize - 1) {
                for (j in 0..mapSize - 1) {
                    if(mapTiles!![i][j]!=null) {
                        if (mapTiles!![i][j].type.equals("forest")) {
                            paint.setColor(Color.rgb(0, 102, 0))
                        } else if (mapTiles!![i][j].type.equals("plains")) {
                            paint.setColor(Color.rgb(128, 255, 128))
                        } else if (mapTiles!![i][j].type.equals("snow")) {
                            paint.setColor(Color.rgb(255, 255, 255))
                        } else if (mapTiles!![i][j].type.equals("desert")) {
                            paint.setColor(Color.rgb(255, 255, 204))
                        } else if (mapTiles!![i][j].type.equals("water")) {
                            paint.setColor(Color.rgb(0, 0, 255))
                        }else if (mapTiles!![i][j].type.equals("land")) {
                            paint.setColor(Color.rgb(100, 100, 100))
                        }
                        /*if(worldMap!![i][j]==1){
                        paint.setColor(Color.rgb(0, 102, 0))
                        }else {
                            paint.setColor(Color.rgb(0, 0, 255))
                        }*/
                        if(j==selectedX&&i==selectedY){
                            paint.setStyle(Paint.Style.STROKE);
                            c.drawRect((j * rectSize).toFloat(), (i * rectSize).toFloat(), ((j * rectSize) + rectSize).toFloat(), ((i * rectSize) + rectSize).toFloat(), paint)
                        }else{
                            paint.setStyle(Paint.Style.FILL);
                            c.drawRect((j * rectSize).toFloat(), (i * rectSize).toFloat(), ((j * rectSize) + rectSize).toFloat(), ((i * rectSize) + rectSize).toFloat(), paint)
                        }
                    }
                }
            }
        }
    }
}