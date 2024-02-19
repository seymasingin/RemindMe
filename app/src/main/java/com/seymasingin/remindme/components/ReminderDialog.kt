package com.seymasingin.remindme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ReminderDialog(
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
)
{
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Info")
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview
@Composable
fun ReminderDialogPreview(){
    ReminderDialog(
        title = "SGHRTHRTJ",
        description = "trjtjj",
        onDismissRequest = {},
        onConfirmation = {})
}




