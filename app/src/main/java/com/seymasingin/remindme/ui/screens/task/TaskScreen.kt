package com.seymasingin.remindme.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority
    val date: String by sharedViewModel.date
    val time: String by sharedViewModel.time

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask,
                navigateToListScreen = {action ->
                    if(action == Action.NO_ACTION){
                        navigateToListScreen(action)
                    }
                    else{
                        if(sharedViewModel.validateFields()){
                            navigateToListScreen(action)
                        }
                        else{
                            displayToast(context)
                        }
                    }
                })
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                date= date,
                onDateChange = {
                    sharedViewModel.updateDate(it)
                },
                time = time,
                onTimeChange = {
                    sharedViewModel.updateTime(it)
                },
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                },
                context
            )
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
}


