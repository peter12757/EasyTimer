package com.eathemeat.easytimer.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class ClockTimeView(context: Context?) : View(context) {

    var time:Int =0
    lateinit var paint:Paint


    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            if (paint == null) {
                paint = Paint()
            }


        }

        super.onDraw(canvas)
    }
}