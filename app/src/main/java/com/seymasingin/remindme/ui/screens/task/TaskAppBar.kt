package com.seymasingin.remindme.ui.screens.task

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.seymasingin.remindme.R
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.util.Action

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen)
    } else {
        ExistingTaskAppBar(selectedTask, navigateToListScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_task),
                color = Color.Black
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.statusBar)
        ),
        actions = {
            DeleteAction(onDeleteClicked = navigateToListScreen)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = Color.Black,
                maxLines = 1
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.statusBar)
        ),
        actions = {
            SendAction(context = LocalContext.current, selectedTask)
            DeleteAction(onDeleteClicked = navigateToListScreen)
        }
    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = {
        onCloseClicked(Action.NO_ACTION)
    }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close_icon),
            tint = Color.Black
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit
) {
    IconButton(onClick = {
        onDeleteClicked(Action.DELETE)
    }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.Black
        )
    }
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = {
        onBackClicked(Action.NO_ACTION)
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back_arrow),
            tint = Color.Black
        )
    }
}

@Composable
fun SendAction(
    context: Context,
    selectedTask: ToDoTask,
) {
    IconButton(
        onClick = {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Görev Adı: ${selectedTask.title}\n\nDetaylar: " +
                        "${selectedTask.description}\n\nTarih: " +
                        "${selectedTask.date}\n\nSaat: " +
                        "${selectedTask.time}"
            )
            intent.setType("text/plain")
            intent.setPackage("com.whatsapp")
            context.startActivity(intent)
        }
    ) {
        Icon(
            painterResource(id = R.drawable.ic_send),
            contentDescription = "",
            tint = Color.Black
        )
    }
}

@Composable
@Preview
fun NewTaskAppBarPreview() {
    NewTaskAppBar(navigateToListScreen = {})
}

@Composable
@Preview
fun ExistingTaskAppBarPrevie() {
    ExistingTaskAppBar(
        selectedTask = ToDoTask(
            0,
            "",
            "",
            Priority.HIGH,
            "",
            "",
            ""),
        navigateToListScreen= {}
    )

}

