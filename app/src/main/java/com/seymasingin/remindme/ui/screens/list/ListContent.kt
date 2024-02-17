package com.seymasingin.remindme.ui.screens.list

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.PRIORITY_INDICATOR_SIZE
import com.seymasingin.remindme.ui.theme.TASK_ITEM_ELEVATION
import com.seymasingin.remindme.util.RequestState
import com.seymasingin.remindme.util.SearchAppBarState

@Composable
fun ListContent(
    tasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriority: List<ToDoTask>,
    highPriority: List<ToDoTask>,
    sortState: RequestState<Priority>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    searchAppBarState: SearchAppBarState,

) {
    if(sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if(searchedTasks is RequestState.Success){
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if(tasks is RequestState.Success){
                    HandleListContent(
                        tasks = tasks.data ,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriority,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriority,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
    }
}


@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,

    ) {
    LazyColumn{
        items(tasks){task ->
            TaskItem(task, navigateToTaskScreen )
        }
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.LightGray,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {navigateToTaskScreen(toDoTask.id)}
        ){
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()) {
            Row{
                Text(
                    modifier = Modifier.weight(8f),
                    text = toDoTask.title,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                    )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                    ){
                    Canvas(modifier = Modifier
                        .size(PRIORITY_INDICATOR_SIZE)
                    ){
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = Color.Black,
                maxLines = 2
            )
        }
    }
}

@Composable
fun HandleListContent(
    tasks:List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if(tasks.isEmpty()){
        EmptyContent()
    }
    DisplayTasks(
        tasks = tasks,
        navigateToTaskScreen = navigateToTaskScreen
        )
}

@Composable
@Preview
fun TaskItemPreview(){
    TaskItem(
        toDoTask = ToDoTask(
            0,
            "title",
            "random",
            Priority.MEDIUM),
        navigateToTaskScreen ={})
}