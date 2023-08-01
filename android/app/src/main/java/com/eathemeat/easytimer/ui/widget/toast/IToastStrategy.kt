package com.eathemeat.easytimer.ui.widget.toast

import android.app.Application




interface IToastStrategy {

    /**
     * 注册策略
     */
    fun registerStrategy(application: Application)

    /**
     * 创建 Toast
     */
    fun createToast(style: IToastStyle<*>?): IToast

    /**
     * 显示 Toast
     */
    fun showToast(params: ToastParams)

    /**
     * 取消 Toast
     */
    fun cancelToast()
}