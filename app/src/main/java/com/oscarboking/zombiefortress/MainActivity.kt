package com.oscarboking.zombiefortress

import android.app.Activity
import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = fragmentManager
        val fragment = FragmentMenu()
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "MENU_FRAGMENT").commit()


    }
}
