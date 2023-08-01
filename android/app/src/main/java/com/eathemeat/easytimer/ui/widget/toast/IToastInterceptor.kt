package com.eathemeat.easytimer.ui.widget.toast

interface IToastInterceptor {

    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    fun intercept(params: ToastParams?): Boolean
}