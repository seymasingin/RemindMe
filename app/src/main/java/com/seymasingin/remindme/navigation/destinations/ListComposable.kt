package com.seymasingin.remindme.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seymasingin.remindme.ui.screens.list.ListScreen
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.Constants.LIST_ARGUMENT_KEY
import com.seymasingin.remindme.util.Constants.LIST_SCREEN
import com.seymasingin.remindme.util.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){ navBackStackEntry ->
        val action  = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        LaunchedEffect(action) {
            sharedViewModel.action.value = action
        }

        ListScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel)
    }
}
