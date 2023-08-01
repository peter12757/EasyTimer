package com.eathemeat.easytimer.ui.widget.toast

import android.content.Context
import android.view.View

class LocationToastStyle:IToastStyle<View> {

    private var mStyle: IToastStyle<*>? = null

    private var mGravity = 0
    private var mXOffset = 0
    private var mYOffset = 0
    private var mHorizontalMargin = 0f
    private var mVerticalMargin = 0f

    constructor(style: IToastStyle<*>?, gravity: Int):this(style, gravity, 0, 0, 0f, 0f) {

    }

    constructor(
        style: IToastStyle<*>?,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        mStyle = style
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun createView(context: Context): View {
        return mStyle!!.createView(context)
    }

    override fun getGravity(): Int {
        return mGravity
    }

    override fun getXOffset(): Int {
        return mXOffset
    }

    override fun getYOffset(): Int {
        return mYOffset
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }
}