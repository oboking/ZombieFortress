package com.oscarboking.zombiefortress

import android.content.Context
import android.os.Environment
import java.io.*
import android.os.Environment.getExternalStorageDirectory





/**
 * Created by boking on 2017-08-26.
 */

class AllSaveStates : Serializable {
    var saveStateList: MutableList<SaveState> = mutableListOf()
}