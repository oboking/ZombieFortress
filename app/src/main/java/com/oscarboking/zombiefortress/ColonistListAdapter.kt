package com.oscarboking.zombiefortress

import android.support.v4.accessibilityservice.AccessibilityServiceInfoCompat.getDescription
import android.support.v4.app.NotificationCompat.getCategory
import android.R.attr.description
import android.widget.TextView
import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter



/**
 * Created by boking on 2017-08-15.
 */
class ColonistListAdapter : ArrayAdapter<Colonist> {

    constructor(context: Context, textViewResourceId: Int) : super(context, textViewResourceId) {}

    constructor(context: Context, resource: Int, items: List<Colonist>) : super(context, resource, items) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v: View? = convertView

        if (v == null) {
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(R.layout.colonist_list_item, null)
        }

        val p = getItem(position)

        if (p != null) {
            val tt1 = v!!.findViewById(R.id.colonistNameView) as TextView
            val tt2 = v!!.findViewById(R.id.colonistCurrentlyDoing) as TextView
            //val tt3 = v.findViewById(R.id.colonistItemsView) as TextView

            tt1?.text = p.getName()
            tt2?.text = p.getCurrentJob()
            //tt3?.setText(p.getDescription())
        }

        return v
    }

}