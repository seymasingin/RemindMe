package com.seymasingin.remindme.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seymasingin.remindme.ui.screens.list.ListScreen
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Constants.LIST_ARGUMENT_KEY
import com.seymasingin.remindme.util.Constants.LIST_SCREEN

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        ListScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel)
    }
}
