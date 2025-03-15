package com.eathemeat.easytimer.ui.widget.calender

class Event(var color:Int ,var timeInMillis:Long,var data:Any? =null ) {




    override fun equals(o:Any?) :Boolean{
        if (this === o) return true
        if (o == null || Event::class != o::class) return false

        var  event = o as Event
        if (color != event.color) return false
        if (timeInMillis != event.timeInMillis) return false
        data?.let {
            if (it != event.data) return false
        }
        return true
    }


    override fun hashCode() :Int {
        var result = color;
        result = 31 * result + (timeInMillis.xor(timeInMillis.shr(32)) as Int)
        data?.let {
            result = 31 * result + it.hashCode()
        }
        return result
    }

    @Override
    override fun toString():String {
        return "Event{color=${color}, timeInMillis=${timeInMillis}, data=${data}}"
    }
}