package com.seymasingin.remindme.ui.screens.task

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.seymasingin.remindme.R
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    if (selectedTask == null) {
        NewTaskAppBar(sharedViewModel, navController)
    } else {
        ExistingTaskAppBar(selectedTask, sharedViewModel, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    TopAppBar(
        navigationIcon = {
            BackAction(navController)
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_task),
                color = colorResource(id = R.color.textcolor)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            DeleteAction(sharedViewModel, navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(navController)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = colorResource(id = R.color.textcolor),
                maxLines = 1
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            SendAction(context = LocalContext.current, selectedTask)
            DeleteAction(sharedViewModel, navController )
        }
    )
}

@Composable
fun CloseAction(
   navController: NavController
) {
    IconButton(onClick = {
        navController.navigateUp()
    }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close_icon),
            tint = colorResource(id = R.color.textcolor)
        )
    }
}

@Composable
fun DeleteAction(
   sharedViewModel: SharedViewModel,
   navController: NavController
) {
    IconButton(onClick = {
        if(sharedViewModel.validateDelete()){
            sharedViewModel.selectedTask.value?.let { sharedViewModel.deleteTask(it) }
            navController.navigateUp()
        }
    }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = colorResource(id = R.color.textcolor)
        )
    }
}

@Composable
fun BackAction(
    navController: NavController
) {
    IconButton(onClick = {
        navController.navigateUp()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back_arrow),
            tint = colorResource(id = R.color.textcolor)
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
            val pm = context.packageManager
            val appInfo: ApplicationInfo? = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getApplicationInfo(
                        "com.whatsapp",
                        PackageManager.ApplicationInfoFlags.of(0)
                    )
                } else {
                    pm.getApplicationInfo("com.whatsapp", 0)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }

            if (appInfo != null) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Note Title: ${selectedTask.title}\n\n" +
                            "Description: ${selectedTask.description}\n\n"
                )
                if (!selectedTask.image.isNullOrEmpty()) {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(selectedTask.image))
                }
                intent.setPackage("com.whatsapp")
                intent.setType("text/plain")
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Please install WhatsApp first.", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Icon(
            painterResource(id = R.drawable.ic_send),
            contentDescription = "",
            tint = colorResource(id = R.color.textcolor)
        )
    }
}

