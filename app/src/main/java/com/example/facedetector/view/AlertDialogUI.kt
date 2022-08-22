package com.example.facedetector.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.facedetector.R

//TODO later Use this compose UI dialog in the permission fragment
@Composable
fun SimpleAlertDialog() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = {
            })
            { Text(text = stringResource(R.string.ok)) }
        },
        title = { Text(text = stringResource(R.string.alert)) },
        text = { Text(text = stringResource(R.string.permission_alert_msg)) }
    )
}
