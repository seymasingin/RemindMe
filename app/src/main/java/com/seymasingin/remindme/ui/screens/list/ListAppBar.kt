package com.seymasingin.remindme.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ContentAlpha
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityItem
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.TOP_APP_BAR_HEIGHT
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.SearchAppBarState
import com.seymasingin.remindme.util.TrailingIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when(searchAppBarState){
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked= {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(it)
                                },
                onDeleteAllClicked = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                }
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChanged = {
                    sharedViewModel.searchTextState.value = it
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = {
                    sharedViewModel.getSearchTasks(query = it)
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text= stringResource(id = R.string.list_screen_title))
        },
        actions= {
            ListAppBarActions(onSearchClicked, onSortClicked, onDeleteAllClicked)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit
){
        SearchAction(onSearchClicked)
        SortAction(onSortClicked)
        DeleteAll (onDeleteClicked)
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit){
    IconButton (onClick= {onSearchClicked()}){
        Icon(imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_action),
            tint = Color.Black
        )
    }
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

@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    var trailingIconState by remember { mutableStateOf(TrailingIconState.READY_TO_DELETE) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        color = MaterialTheme.colorScheme.primary

    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text= stringResource(id = R.string.search_placeholder), color = Color.Black) },
            textStyle =
                TextStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}){
                    Icon(
                        modifier= Modifier.alpha(ContentAlpha.disabled),
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = Color.Black
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        when(trailingIconState){
                            TrailingIconState.READY_TO_DELETE -> {
                                onTextChanged("")
                                trailingIconState = TrailingIconState.READY_TO_CLOSE
                            }
                            TrailingIconState.READY_TO_CLOSE -> {
                                if(text.isNotEmpty()){
                                    onTextChanged("")
                                }else {
                                    onCloseClicked()
                                    trailingIconState = TrailingIconState.READY_TO_DELETE
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = Color.Black
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
@Preview
fun DefaultListAppBarPreview(){
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllClicked = {}
    )
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "",
        onTextChanged = {},
        onCloseClicked = { /*TODO*/ },
        onSearchClicked = {}
    )
}
