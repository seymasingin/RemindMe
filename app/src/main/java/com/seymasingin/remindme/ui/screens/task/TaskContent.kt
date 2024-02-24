package com.seymasingin.remindme.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityDropdown
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.notification.ReminderWorker
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING
import com.seymasingin.remindme.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import com.seymasingin.remindme.util.Tools
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    date: String,
    onDateChange: (String) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    context: Context
) {
    val datePickerState = rememberDatePickerState()
    val showDateDialog = rememberSaveable { mutableStateOf(false) }
    val dateState = remember { mutableStateOf("") }

    val showTimeDialog = rememberSaveable { mutableStateOf(false) }
    val timeState = remember { mutableStateOf("") }
    val state = rememberTimePickerState(is24Hour = false)
    var finalTime by remember { mutableStateOf("") }
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    //val reminderTimeMillis by remember { mutableLongStateOf(0L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {onTitleChange(it)},
            label = { Text(text= stringResource(id = R.string.title) )},
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colorScheme.background
        )
        PriorityDropdown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        Row(){
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MEDIUM_PADDING, end= 4.dp)

                    .height(PRIORITY_DROPDOWN_HEIGHT),
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { showDateDialog.value = true },

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "",
                        tint = Color.Black,
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.Black,
                        text = date,
                        fontSize = 16.sp
                    )
                }
            }

            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MEDIUM_PADDING, start = 2.dp)
                    .height(PRIORITY_DROPDOWN_HEIGHT),
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { showTimeDialog.value = true },

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "Time",
                        tint = Color.Black,
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        color = Color.Black,
                        text = time,
                        fontSize = 16.sp
                    )
                }
            }
        }



        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = {onDescriptionChange(it)},
            label = { Text(text= stringResource(id = R.string.description) )},
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        if (showDateDialog.value) {
            DatePickerDialog(
                onDismissRequest = { showDateDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDateDialog.value = false
                            val dateTime = Tools.convertLongToTime(datePickerState.selectedDateMillis!!)
                            dateState.value = dateTime
                            onDateChange(dateState.value)


                        },
                        ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    dateFormatter = remember { DatePickerFormatter() },
                    title = {
                        DatePickerDefaults.DatePickerTitle(
                            datePickerState,
                            modifier = Modifier.padding(all= 2.dp)
                        )
                    },
                    headline = {
                        DatePickerDefaults.DatePickerHeadline(
                            datePickerState,
                            remember { DatePickerFormatter() },
                            modifier = Modifier.padding(all= 2.dp)
                        )
                    }
                )
            }
        }
        if (showTimeDialog.value) {
            TimePickerDialog(
                onCancel = { showTimeDialog.value = false },
                onConfirm = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, state.hour)
                    cal.set(Calendar.MINUTE, state.minute)
                    cal.isLenient = false
                    finalTime = formatter.format(cal.time)
                    showTimeDialog.value = false
                    timeState.value = finalTime

                    onTimeChange(timeState.value)

                    val dateTimeString = "${dateState.value} ${timeState.value}"
                    val x = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                    val dateTime = x.parse(dateTimeString)
                    val reminderTimeMillis = dateTime?.time ?: 0L

                    setReminder(title, context, reminderTimeMillis)

                },
            ) {
                TimeInput(state = state)
            }
        }
    }
}

private fun setReminder(reminderText: String, context: Context, reminderTimeMillis: Long) {

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val inputData = Data.Builder()
        .putString("reminderText", reminderText)
        .build()

    val request = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setConstraints(constraints)
        .setInputData(inputData)
        .setInitialDelay(reminderTimeMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(request)
}




