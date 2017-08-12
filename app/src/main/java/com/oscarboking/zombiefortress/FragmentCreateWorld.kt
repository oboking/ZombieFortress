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

class FragmentCreateWorld : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragmentcreateworld, container, false)
        val buttonGenerate : Button = root.findViewById(R.id.buttonGenerate) as Button


        buttonGenerate.setOnClickListener {
            val fragmentManager = fragmentManager
            val fragment = GameFragment()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "GAME_FRAGMENT").commit()
        }
        return root
    }
}