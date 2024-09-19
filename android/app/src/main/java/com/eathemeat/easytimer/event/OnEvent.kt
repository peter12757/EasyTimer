package com.eathemeat.easytimer.event

import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data

interface OnEvent {

    fun onEvent(type: EventType, data: Bundle = Bundle()): Boolean
}