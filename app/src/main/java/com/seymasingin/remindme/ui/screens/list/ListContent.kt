package com.seymasingin.remindme.ui.screens.list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import com.seymasingin.remindme.R
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING
import com.seymasingin.remindme.ui.theme.TASK_ITEM_ELEVATION
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.RequestState

@Composable
fun ListContent(
    tasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriority: List<ToDoTask>,
    highPriority: List<ToDoTask>,
    sortState: RequestState<Priority>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onDeleteClicked: (Action, ToDoTask) -> Unit,
    context: Context
) {
    if(sortState is RequestState.Success){
        when{
            searchedTasks is RequestState.Success -> {
                HandleListContent(
                    tasks = searchedTasks.data,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onDeleteClicked,
                    context
                )
            }

            sortState.data == Priority.NONE -> {
                if(tasks is RequestState.Success){
                    HandleListContent(
                        tasks = tasks.data ,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onDeleteClicked,
                        context
                    )
                }
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onDeleteClicked,
                    context
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onDeleteClicked,
                    context
                )
            }
        }
    }
}

@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onDeleteClicked: (Action, ToDoTask) -> Unit,
    context: Context
    ) {
    LazyColumn{
        items(tasks){task ->
            TaskItem(task, navigateToTaskScreen, onDeleteClicked, context )
        }
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onDeleteClicked: (Action, ToDoTask) -> Unit,
    context: Context
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MEDIUM_PADDING),
        color = colorResource(id = R.color.taskColor),
        shape = MaterialTheme.shapes.extraSmall,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {navigateToTaskScreen(toDoTask.id)}
        ){
        Row(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.weight(1f)) {
                Canvas(modifier = Modifier.size(11.dp, 120.dp)
                ){
                    drawRect(
                        color = toDoTask.priority.color
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(all = LARGE_PADDING)
                    .fillMaxWidth()
                    .weight(20f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        modifier = Modifier.weight(8f),
                        text = toDoTask.title,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    IconButton(onClick = { onDeleteClicked(Action.DELETE, toDoTask) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.TopEnd
                        ){
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                                tint= Color.DarkGray)
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        modifier = Modifier.weight(8f),
                        text = toDoTask.description,
                        color = Color.Black,
                        maxLines = 2
                    )
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.putExtra(Intent.EXTRA_TEXT, "Görev Adı: ${toDoTask.title}\n\nDetaylar: ${toDoTask.description}\n\nTarih: ${toDoTask.date}\n\nSaat: ${toDoTask.time}")
                            intent.setType("text/plain")
                            intent.setPackage("com.whatsapp")
                            context.startActivity(intent)
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ){
                            Icon(
                                painterResource(id = R.drawable.ic_send),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "",
                                tint= Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HandleListContent(
    tasks:List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onDeleteClicked: (Action, ToDoTask) -> Unit,
    context: Context
) {
    if(tasks.isEmpty()){
        EmptyContent()
    }
    DisplayTasks(
        tasks = tasks,
        navigateToTaskScreen = navigateToTaskScreen,
        onDeleteClicked = onDeleteClicked,
        context
    )
}

@Composable
fun SearchView(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(all = MEDIUM_PADDING)
            .background(color = Color.White),
        value = text,
        onValueChange = {
            onTextChanged(it)
            sharedViewModel.getSearchTasks(query = it)
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
            Icon(
                modifier= Modifier.alpha(ContentAlpha.disabled),
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                tint = Color.Black
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    onTextChanged("")
                    onCloseClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = Color.Black
                )
            }
        },
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

@Composable
@Preview
fun TaskItemPreview() {
    TaskItem(
        toDoTask = ToDoTask(0, "HOMEWORK", "RemindMe", Priority.HIGH, "", "", ""),
        navigateToTaskScreen = {},
        onDeleteClicked = { action, task -> },
        context = LocalContext.current
    )
}



