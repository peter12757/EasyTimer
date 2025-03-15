package com.eathemeat.easytimer.ui.widget.calender

class Events(var timeInMillis:Long,var events:MutableList<Event>) {

    override fun toString() :String {
        return "Events{events=$events, timeInMillis=$events}"
    }
}