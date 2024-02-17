package com.seymasingin.remindme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING
import com.seymasingin.remindme.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityItem(priority: Priority) {
    Row (verticalAlignment = Alignment.CenterVertically){
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)){
            drawCircle(color= priority.color)
        }
        Text(
            modifier = Modifier.padding(start= MEDIUM_PADDING),
            text = priority.name,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview
fun PriorityItemPreview(){
    PriorityItem(priority = Priority.HIGH)
}