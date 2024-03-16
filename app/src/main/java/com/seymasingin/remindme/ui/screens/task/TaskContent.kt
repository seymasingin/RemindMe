package com.seymasingin.remindme.ui.screens.task

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.seymasingin.remindme.R
import com.seymasingin.remindme.components.PriorityDropdown
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.notification.ReminderWorker
import com.seymasingin.remindme.ui.theme.LARGE_PADDING
import com.seymasingin.remindme.ui.theme.MEDIUM_PADDING
import com.seymasingin.remindme.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import com.seymasingin.remindme.util.Tools
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnrememberedMutableState")
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
    context: Context,
    navController: NavHostController,
    image: String,
    onImageChange: (String) -> Unit,
) {

    val showDateDialog = rememberSaveable { mutableStateOf(false) }
    val dateResult = remember { mutableStateOf("") }

    var showTimePicker by remember { mutableStateOf(false) }
    var finalTime by remember { mutableStateOf("") }
    val state = rememberTimePickerState(is24Hour = true)
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onImageChange(uri.toString())
            }
        }
    )

    val hasNotificationPermission = LocalContext.current.let {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            navController.navigateUp()
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(all = LARGE_PADDING)
            .clickable { keyboardController?.hide() }

    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {onTitleChange(it)},
            label = { Text(text= stringResource(id = R.string.title) )},
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done)
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colorScheme.background
        )
        PriorityDropdown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        Row{
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MEDIUM_PADDING, end = 4.dp)

                    .height(PRIORITY_DROPDOWN_HEIGHT),
                shape = MaterialTheme.shapes.extraSmall,
                onClick = {
                    showDateDialog.value = true
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "",
                        tint = colorResource(id = R.color.textcolor),
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        color = colorResource(id = R.color.textcolor),
                        text = date,
                        fontSize = 13.sp
                    )
                }
            }

            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MEDIUM_PADDING, start = 2.dp)
                    .height(PRIORITY_DROPDOWN_HEIGHT),
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { showTimePicker = true },

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "Time",
                        tint = colorResource(id = R.color.textcolor),
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        color = colorResource(id = R.color.textcolor),
                        text = time,
                        fontSize = 16.sp
                    )
                }
            }
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MEDIUM_PADDING, end = 4.dp)
                .height(PRIORITY_DROPDOWN_HEIGHT),
            shape = MaterialTheme.shapes.extraSmall,
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_add_photo),
                    contentDescription = "",
                    tint = colorResource(id = R.color.textcolor),
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    color = colorResource(id = R.color.textcolor),
                    text = "Add Photo",
                    fontSize = 16.sp
                )
                if (image.isNotEmpty()) {
                    Icon(
                        painterResource(id = R.drawable.ic_attachment),
                        contentDescription = "",
                        tint = colorResource(id = R.color.textcolor),
                        modifier = Modifier.clickable {
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Box {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize()
                .weight(7f),
            value = description,
            onValueChange = {onDescriptionChange(it)},
            label = { Text(text= stringResource(id = R.string.description) )},
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )


        if (showDateDialog.value) {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = derivedStateOf { true }
            DatePickerDialog(
                onDismissRequest = {
                    showDateDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDateDialog.value = false
                            val dateTime = datePickerState.selectedDateMillis?.let {
                                Tools.convertLongToTime(it)
                            }
                            if (dateTime != null) {
                                dateResult.value = dateTime
                            }
                            onDateChange(dateResult.value)
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDateDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, state.hour)
                    cal.set(Calendar.MINUTE, state.minute)
                    cal.isLenient = false
                    finalTime = formatter.format(cal.time)
                    snackScope.launch {
                        snackState.showSnackbar("Entered time: $finalTime")
                    }
                    showTimePicker = false

                    onTimeChange(finalTime)

                    val reminderTimeMillis = cal.timeInMillis


                    if (hasNotificationPermission) {
                        setReminder(title, context, reminderTimeMillis)
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
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

