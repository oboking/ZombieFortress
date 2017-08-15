package com.oscarboking.zombiefortress

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton

/**
 * Created by boking on 2017-08-12.
 */

class FragmentCreateWorld : Fragment() {

    private lateinit var difficultyEasy : RadioButton
    private lateinit var difficultyNormal : RadioButton
    private lateinit var difficultyHard : RadioButton

    private lateinit var sizeSmall : RadioButton
    private lateinit var sizeMedium : RadioButton
    private lateinit var sizeLarge : RadioButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragmentcreateworld, container, false)
        val buttonGenerate : Button = root.findViewById(R.id.buttonGenerate) as Button
        difficultyEasy = root.findViewById(R.id.difficultyEasyRadio) as RadioButton
        difficultyNormal = root.findViewById(R.id.difficultyNormalRadio) as RadioButton
        difficultyHard = root.findViewById(R.id.difficultyHardRadio) as RadioButton

        sizeSmall = root.findViewById(R.id.sizeSmallRadio) as RadioButton
        sizeMedium = root.findViewById(R.id.sizeMediumRadio) as RadioButton
        sizeLarge = root.findViewById(R.id.sizeLargeRadio) as RadioButton

        buttonGenerate.setOnClickListener {

            var difficulty : String = ""
            var worldSize : Int = 20

            if(difficultyEasy.isChecked){
                difficulty="easy"
            }else if (difficultyNormal.isChecked){
                difficulty="normal"
            }else if (difficultyHard.isChecked){
                difficulty="hard"
            }

            if(sizeSmall.isChecked){
                worldSize=12
            }else if (sizeMedium.isChecked){
                worldSize=20
            }else if (sizeLarge.isChecked){
                worldSize=35
            }

            val fragmentManager = fragmentManager
            val fragment = GameFragment()

            val args = Bundle()
            args.putString("difficulty", difficulty)
            args.putInt("worldSize", worldSize)
            fragment.setArguments(args)

            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment as Fragment, "GAME_FRAGMENT").commit()
        }
        return root
    }
}