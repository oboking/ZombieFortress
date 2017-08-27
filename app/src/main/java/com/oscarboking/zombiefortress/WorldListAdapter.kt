package com.oscarboking.zombiefortress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by boking on 2017-08-26.
 */

class WorldListAdapter : ArrayAdapter<SaveState> {

    constructor(context: Context, textViewResourceId: Int) : super(context, textViewResourceId) {}

    constructor(context: Context, resource: Int, saveStates: List<SaveState>) : super(context, resource, saveStates) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v: View? = convertView

        if (v == null) {
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(R.layout.list_item_world, null)
        }

        val p = getItem(position)

        if (p != null) {
            val tt1 = v!!.findViewById(R.id.worldListNameText) as TextView
            val tt2 = v!!.findViewById(R.id.worldListMapView) as MapView
            //val tt3 = v.findViewById(R.id.colonistItemsView) as TextView

            tt1?.text = p.name
            //initiera mapview
            //tt2?.text = p.getQuantity().toString()
            //tt3?.setText(p.getDescription())mapView.mapTiles=generator.mapTiles
            tt2.mapTiles=p.generator.mapTiles
            tt2.updateMapSize(p.mapSize)
            tt2.worldMap=p.generator.worldMap
            tt2.invalidate()
        }

        return v
    }

}