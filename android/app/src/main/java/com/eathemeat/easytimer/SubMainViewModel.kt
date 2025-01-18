package com.eathemeat.easytimer

import android.util.Log
import androidx.lifecycle.ViewModel

class SubMainViewModel : ViewModel(){
    val TAG = SubMainViewModel::class.java.name

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }
}