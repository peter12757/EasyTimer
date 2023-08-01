package com.eathemeat.easytimer.ui.widget.toast

import android.app.Application
import android.view.View
import android.widget.TextView
import android.widget.Toast


open class SystemToast: Toast,IToast {

    /** 吐司消息 View  */
    private var mMessageView: TextView? = null

    constructor(application: Application):super(application) {

    }

    override fun setView(view: View?) {
        super.setView(view)
        if (view == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun setText(text: CharSequence?) {
        super.setText(text)
        if (mMessageView == null) {
            return
        }
        mMessageView!!.text = text
    }
}