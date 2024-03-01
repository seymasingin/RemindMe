package com.seymasingin.remindme.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seymasingin.remindme.ui.screens.task.TaskScreen
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.Constants.TASK_ARGUMENT_KEY
import com.seymasingin.remindme.util.Constants.TASK_SCREEN

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){ navbackStackEntry ->
        val taskId = navbackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)

        LaunchedEffect(taskId){
            sharedViewModel.getSelectedTask(taskId)
        }

        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        LaunchedEffect(selectedTask){
            if(selectedTask != null || taskId == -1){
                sharedViewModel.updateTaskFields(selectedTask)
            }
        }
        TaskScreen( selectedTask, sharedViewModel, navigateToListScreen )
    }
}