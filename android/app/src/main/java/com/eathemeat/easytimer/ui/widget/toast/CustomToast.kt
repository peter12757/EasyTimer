package com.eathemeat.easytimer.ui.widget.toast

import android.R
import android.view.View
import android.widget.TextView




abstract class CustomToast:IToast {

    /** Toast 布局  */
    private var mView: View? = null

    /** Toast 消息 View  */
    private var mMessageView: TextView? = null

    /** Toast 显示重心  */
    private var mGravity = 0

    /** Toast 显示时长  */
    private var mDuration = 0

    /** 水平偏移  */
    private var mXOffset = 0

    /** 垂直偏移  */
    private var mYOffset = 0

    /** 水平间距  */
    private var mHorizontalMargin = 0f

    /** 垂直间距  */
    private var mVerticalMargin = 0f

    /** Toast 动画  */
    private var mAnimations = R.style.Animation_Toast

    /** 短吐司显示的时长，参考至 NotificationManagerService.SHORT_DELAY  */
    private var mShortDuration:Long = 2000

    /** 长吐司显示的时长，参考至 NotificationManagerService.LONG_DELAY  */
    private var mLongDuration:Long = 3500

    override fun setText(id: Int) {
        setText(mView?.getResources()?.getString(id))
    }

    override fun setText(text: CharSequence?) {
        if (mMessageView == null) {
            return
        }
        mMessageView!!.text = text
    }

    override fun setView(view: View?) {
        mView = view
        if (mView == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun getView(): View? {
        return mView
    }

    override fun setDuration(duration: Int) {
        mDuration = duration
    }

    override fun getDuration(): Int {
        return mDuration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
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

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }

    fun setAnimationsId(animationsId: Int) {
        mAnimations = animationsId
    }

    fun getAnimationsId(): Int {
        return mAnimations
    }

    fun setShortDuration(duration: Long) {
        mShortDuration = duration
    }

    fun getShortDuration(): Long {
        return mShortDuration
    }

    fun setLongDuration(duration: Long) {
        mLongDuration = duration
    }

    fun getLongDuration(): Long {
        return mLongDuration
    }
}