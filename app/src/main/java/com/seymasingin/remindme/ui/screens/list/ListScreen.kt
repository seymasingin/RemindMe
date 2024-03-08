package com.seymasingin.remindme.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.seymasingin.remindme.R
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
)
   {
    LaunchedEffect(key1 = true){
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }

    val action by sharedViewModel.action

    val allTasks = sharedViewModel.allTasks.collectAsState()
    val searchedTasks = sharedViewModel.searchTasks.collectAsState()

    val sortState = sharedViewModel.sortState.collectAsState()
    val lowPriority = sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriority = sharedViewModel.highPriorityTasks.collectAsState()

    val searchTextState: String by sharedViewModel.searchTextState

    val snackBarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackBarHostState,
        handleDatabaseActions = {sharedViewModel.handleDatabaseActions(action)},
        sharedViewModel.title.value,
        action,
        onUndoClicked = {sharedViewModel.action.value = it },
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ListAppBar(sharedViewModel)
        } ,
        floatingActionButton = {
            ListFab(
                onFabClick = navigateToTaskScreen
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            SearchView(
                text = searchTextState,
                onTextChanged = {
                    sharedViewModel.searchTextState.value = it
                    sharedViewModel.getSearchTasks(it)
                },
                onCloseClicked = {
                    sharedViewModel.searchTextState.value = ""
                    sharedViewModel.getAllTasks()
                },
                sharedViewModel
            )
            ListContent(
                tasks = allTasks.value,
                lowPriority = lowPriority.value,
                highPriority = highPriority.value,
                sortState = sortState.value,
                searchedTasks = searchedTasks.value,
                navigateToTaskScreen = navigateToTaskScreen,
                onDeleteClicked = {action, task ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                },
                context = LocalContext.current
                )
        }
    }
}

@Composable
fun ListFab(onFabClick: (taskId: Int) -> Unit){
    FloatingActionButton(
        onClick = { onFabClick(-1) },
        containerColor = colorResource(id = R.color.fabColor)
    ) {
        Icon(
            imageVector = Icons.Filled.Add ,
            contentDescription = stringResource(id = R.string.add_button),
            tint= colorResource(id = R.color.textcolor)
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(action){
        if(action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = SetMessage(action, taskTitle), sectionLabel()
                )
            }
        }
    }
}

private fun SetMessage(action:Action, taskTitle: String): String {
    return when(action){
        Action.DELETE_ALL -> "All Tasks Removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun sectionLabel(): String {
    return "OK"
}




