package com.eathemeat.easytimer.ui.widget.toast

class ToastParams {

    /** 显示的文本  */
    var text: CharSequence? = null

    /**
     * Toast 显示时长，有两种值可选
     *
     * 短吐司：[android.widget.Toast.LENGTH_SHORT]
     * 长吐司：[android.widget.Toast.LENGTH_LONG]
     */
    var duration = -1

    /** 延迟显示时间  */
    var delayMillis: Long = 0

    /** Toast 样式  */
    var style: IToastStyle<*>? = null

    /** Toast 处理策略  */
    var strategy: IToastStrategy? = null

    /** Toast 拦截器  */
    var interceptor: IToastInterceptor? = null
}