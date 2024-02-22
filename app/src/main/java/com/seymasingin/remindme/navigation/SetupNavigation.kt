package com.seymasingin.remindme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.seymasingin.remindme.navigation.destinations.listComposable
import com.seymasingin.remindme.navigation.destinations.taskComposable
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,

    ){
    val screen = remember(navController){
        Screens(navController)
    }

    NavHost(navController = navController, startDestination = LIST_SCREEN){
        listComposable(
            screen.task, sharedViewModel
        )
        taskComposable(
            sharedViewModel,
            screen.list
        )
    }
}