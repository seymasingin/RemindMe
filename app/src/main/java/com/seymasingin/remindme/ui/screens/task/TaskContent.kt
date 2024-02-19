package com.seymasingin.remindme.ui.screens.task

import android.annotation.SuppressLint
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
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityDropdown
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING
import com.seymasingin.remindme.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import com.seymasingin.remindme.util.Tools
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val showDateDialog = rememberSaveable { mutableStateOf(false) }
    val dateState = remember { mutableStateOf("Date") }

    val showTimeDialog = rememberSaveable { mutableStateOf(false) }
    val timeState = remember { mutableStateOf("Time") }
    val state = rememberTimePickerState(is24Hour = true)
    var finalTime by remember { mutableStateOf("") }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    //val scheduler = AndroidAlarmSchedular(context = LocalContext.current)
    //var alarmItem: AlarmItem?

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

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MEDIUM_PADDING)
                .height(PRIORITY_DROPDOWN_HEIGHT),
            shape = MaterialTheme.shapes.extraSmall,
            onClick = { showDateDialog.value = true },

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "",
                    tint = Color.Black,
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    color = Color.Black,
                    text = dateState.value,
                    fontSize = 16.sp
                )
            }
        }

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MEDIUM_PADDING)
                .height(PRIORITY_DROPDOWN_HEIGHT),
            shape = MaterialTheme.shapes.extraSmall,
            onClick = { showTimeDialog.value = true },

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "",
                    tint = Color.Black,
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    color = Color.Black,
                    text = timeState.value,
                    fontSize = 16.sp
                )
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
                            val date = Tools.convertLongToTime(datePickerState.selectedDateMillis!!)
                            dateState.value = date

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

                    /*val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
                    val date = LocalDate.parse(dateState.value, dateFormatter)

                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm a", Locale.getDefault())
                    val time = LocalTime.parse(finalTime, timeFormatter)

                    val localDateTime = LocalDateTime.of(date, time)

                    alarmItem = AlarmItem(
                        title = title,
                        description = description,
                        time = localDateTime
                    )
                    alarmItem?.let(scheduler::schedule)*/

                },
            ) {
                TimeInput(state = state)
            }
        }
        }
    }