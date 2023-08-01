package com.eathemeat.easytimer.ui.widget.toast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View


class CustomToastStyle : IToastStyle<View> {

    private var mLayoutId = 0
    private var mGravity = 0
    private var mXOffset = 0
    private var mYOffset = 0
    private var mHorizontalMargin = 0f
    private var mVerticalMargin = 0f

    constructor(id: Int):this(id, Gravity.CENTER) {

    }

    constructor(id: Int, gravity: Int):this(id, gravity, 0, 0) {
    }

    constructor(id: Int, gravity: Int, xOffset: Int, yOffset: Int):this(id, gravity, xOffset, yOffset, 0f, 0f) {

    }

    constructor(
        id: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        mLayoutId = id
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(mLayoutId, null)
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