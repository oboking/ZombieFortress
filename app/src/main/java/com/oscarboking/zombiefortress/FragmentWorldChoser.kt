package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Created by boking on 2017-08-12.
 */

class FragmentWorldChoser : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragmentworldchoser, container, false)
        val buttonNewWorld : Button = root.findViewById(R.id.buttonNewWorld) as Button

        buttonNewWorld.setOnClickListener {
            val fragmentManager = fragmentManager
            val fragment = FragmentCreateWorld()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "WORLDCREATE_FRAGMENT").commit()
        }
        return root
    }
}