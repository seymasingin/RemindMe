package com.seymasingin.remindme.ui.screens.task

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.seymasingin.remindme.R
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.viewmodel.SharedViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority
    val date: String by sharedViewModel.date
    val time: String by sharedViewModel.time
    val image: String by sharedViewModel.image

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask,
                sharedViewModel,
                navController
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)

        ){
            Box(modifier = Modifier.weight(8f)){
                TaskContent(
                    title = title,
                    onTitleChange = {
                        sharedViewModel.updateTitle(it)
                    },
                    description = description,
                    onDescriptionChange = {
                        sharedViewModel.description.value = it
                    },
                    priority = priority,
                    date= date,
                    onDateChange = {
                        sharedViewModel.updateDate(it)
                    },
                    time = time,
                    onTimeChange = {
                        sharedViewModel.updateTime(it)
                    },
                    onPrioritySelected = {
                        sharedViewModel.priority.value = it
                    },
                    context,
                    navController = NavHostController(context),
                    image= image,
                    onImageChange = {
                        sharedViewModel.updateImage(it)
                    },
                )
            }
            ElevatedButton(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(all = LARGE_PADDING)
                    .weight(1f)
                ,
                onClick = {
                    if(selectedTask == null){
                        if(sharedViewModel.validateFields()){
                            sharedViewModel.addTask()
                            navController.navigateUp()
                        }
                        else{
                            displayToast(context)
                        }
                    }else{
                        sharedViewModel.updateTask()
                        navController.navigateUp()
                    }
                },
                elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = colorResource(id = R.color.fabColor)
                ),
            ){
                Text(
                    text = "SAVE NOTE",
                    color = colorResource(id = R.color.textcolor),
                    fontSize = 20.sp
                )
            }
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
}




