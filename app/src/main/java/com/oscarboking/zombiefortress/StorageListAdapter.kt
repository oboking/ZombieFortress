package com.oscarboking.zombiefortress

import android.support.v4.accessibilityservice.AccessibilityServiceInfoCompat.getDescription
import android.support.v4.app.NotificationCompat.getCategory
import android.R.attr.description
import android.widget.TextView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.oscarboking.zombiefortress.Item



/**
 * Created by boking on 2017-08-15.
 */
class StorageListAdapter : ArrayAdapter<Item> {

    constructor(context: Context, textViewResourceId: Int) : super(context, textViewResourceId) {}

    constructor(context: Context, resource: Int, items: List<Item>) : super(context, resource, items) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v: View? = convertView

        if (v == null) {
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(R.layout.list_item_storage, null)
        }

        val p = getItem(position)

        if (p != null) {
            val tt1 = v!!.findViewById(R.id.storageItemNameView) as TextView
            val tt2 = v!!.findViewById(R.id.storageItemQuantityView) as TextView
            //val tt3 = v.findViewById(R.id.colonistItemsView) as TextView

            tt1?.text = p.getName()
            tt2?.text = p.getQuantity().toString()
            //tt3?.setText(p.getDescription())
        }

        return v
    }

}