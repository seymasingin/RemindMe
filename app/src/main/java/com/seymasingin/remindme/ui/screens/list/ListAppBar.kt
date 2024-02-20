package com.seymasingin.remindme.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityItem
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
) {
    DefaultListAppBar(
        onSortClicked = {
            sharedViewModel.persistSortState(it)
                        },
        onDeleteAllClicked = {
            sharedViewModel.action.value = Action.DELETE_ALL
        }
    )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text= stringResource(id = R.string.list_screen_title))
        },
        actions= {
            ListAppBarActions(onSortClicked, onDeleteAllClicked)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.statusBar)
        )
    )
}

@Composable
fun ListAppBarActions(
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit
){
        SortAction(onSortClicked)
        DeleteAll (onDeleteClicked)
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit){
    var expanded by remember {mutableStateOf(false)}

    IconButton (onClick= {expanded = true}){
        Icon(painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                    text = {PriorityItem(priority = Priority.LOW)},
                    onClick = {
                        expanded = false
                        onSortClicked(Priority.LOW)
                    }
            )
            DropdownMenuItem(
                text = {PriorityItem(priority = Priority.HIGH)},
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                }
            )
            DropdownMenuItem(
                text = {PriorityItem(priority = Priority.NONE)},
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                }
            )

            }
        }
    }

@Composable
fun DeleteAll( onDeleteClicked: () -> Unit) {
    var expanded by remember {mutableStateOf(false)}

    IconButton (onClick= {expanded = true}){
        Icon(painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ){
            DropdownMenuItem(
                modifier = Modifier.padding(start = LARGE_PADDING),
                text = { Text(text = stringResource(id = R.string.delete_all_action), color = Color.Black) },

                onClick = {
                    expanded = false
                    onDeleteClicked
                }
            )
        }
    }
}
