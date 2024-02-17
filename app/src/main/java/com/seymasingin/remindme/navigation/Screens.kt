package com.seymasingin.remindme.navigation

import androidx.navigation.NavController
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.Constants.LIST_SCREEN
import com.seymasingin.remindme.util.Constants.TASK_SCREEN

class Screens(navController: NavController) {
    val list: (Action) -> Unit = {action ->
        navController.navigate("list/${action.name}"){
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }

    val task: (Int) -> Unit = {taskId ->
        navController.navigate("task/$taskId")
    }
 }