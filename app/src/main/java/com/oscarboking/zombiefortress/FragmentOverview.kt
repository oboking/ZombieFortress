package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView


/**
 * Created by boking on 2017-08-14.
 */
class FragmentOverview : Fragment() {

    private lateinit var listNewsView: ListView
    var newsList: MutableList<String> = mutableListOf<String>()
    private lateinit var parentView : View
    private var listener: CallBack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_overview, container, false)
        newsList= mutableListOf("ayy","lmao")

        return root
    }

    fun addNews(message : String){
        newsList.add(0,"You have started a colony...")
        (listNewsView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        listNewsView.smoothScrollToPosition(0)
    }

    fun setCustomObjectListener(listener: CallBack) {
        this.listener = listener
    }

    interface CallBack {
        // or when data has been loaded
        fun callBackMethod(data: Int)
    }


    class CallBackImpl : CallBack{
        override fun callBackMethod(data: Int) {
            //ive been called back
        }

    }
}