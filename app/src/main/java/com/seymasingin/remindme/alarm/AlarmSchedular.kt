package com.seymasingin.remindme.alarm

import com.seymasingin.remindme.data.models.AlarmItem

interface AlarmSchedular {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}