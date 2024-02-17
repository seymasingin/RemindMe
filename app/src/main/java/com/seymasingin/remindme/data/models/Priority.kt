package com.seymasingin.remindme.data.models

import androidx.compose.ui.graphics.Color
import com.seymasingin.remindme.ui.theme.HighPriorityColor
import com.seymasingin.remindme.ui.theme.LowPriorityColor
import com.seymasingin.remindme.ui.theme.MediumPriorityColor
import com.seymasingin.remindme.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}