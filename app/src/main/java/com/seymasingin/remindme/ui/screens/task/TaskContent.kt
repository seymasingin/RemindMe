package com.seymasingin.remindme.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityDropdown
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {onTitleChange(it)},
            label = { Text(text= stringResource(id = R.string.title) )},
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colorScheme.background
        )
        PriorityDropdown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = {onDescriptionChange(it)},
            label = { Text(text= stringResource(id = R.string.description) )},
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
@Preview
fun TaskContentPreview(){
    TaskContent(
        title = "eagrerr",
        onTitleChange = {},
        description = "agraegr",
        onDescriptionChange = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}