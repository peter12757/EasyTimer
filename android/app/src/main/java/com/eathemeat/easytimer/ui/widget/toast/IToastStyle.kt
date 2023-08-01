package com.eathemeat.easytimer.ui.widget.toast

import android.content.Context
import android.view.Gravity
import android.view.View


interface IToastStyle<V:View> {

    /**
     * 创建 Toast 视图
     */
    fun createView(context: Context): V

    /**
     * 获取 Toast 显示重心
     */
    fun getGravity(): Int {
        return Gravity.CENTER
    }

    /**
     * 获取 Toast 水平偏移
     */
    fun getXOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 垂直偏移
     */
    fun getYOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 水平间距
     */
    fun getHorizontalMargin(): Float {
        return 0F
    }

    /**
     * 获取 Toast 垂直间距
     */
    fun getVerticalMargin(): Float {
        return 0F
    }
}