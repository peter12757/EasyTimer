package com.eathemeat.easytimer.ui.widget.toast

import android.app.Activity




class ActivityToast: CustomToast {

    /** Toast 实现类  */
    private lateinit var mToastImpl: ToastImpl

    constructor(activity: Activity) {
        mToastImpl = ToastImpl(activity, this)
    }

    override fun show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }
}