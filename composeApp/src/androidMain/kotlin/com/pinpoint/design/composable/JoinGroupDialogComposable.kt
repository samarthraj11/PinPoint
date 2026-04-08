package com.pinpoint.design.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.PinPointTheme

@Composable
fun JoinGroupDialogComposable(
    groupId: String,
    onGroupIdChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LocalColors.SurfaceDark,
        titleContentColor = LocalColors.TextPrimary,
        textContentColor = LocalColors.TextSecondary,
        title = {
            Text(text = "Join Group", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = "Enter the group ID shared by the group creator.",
                    fontSize = 14.sp,
                    color = LocalColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = groupId,
                    onValueChange = onGroupIdChange,
                    label = { Text("Group ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalColors.Primary,
                        cursorColor = LocalColors.Primary,
                        focusedLabelColor = LocalColors.Primary,
                        unfocusedBorderColor = LocalColors.BorderMedium,
                        unfocusedLabelColor = LocalColors.TextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = LocalColors.Primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = LocalColors.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Join", color = LocalColors.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LocalColors.TextSecondary)
            }
        }
    )
}

@Preview(name = "Join Group Dialog — Idle")
@Composable
private fun PreviewJoinGroupDialog() {
    PinPointTheme {
        JoinGroupDialogComposable(
            groupId = "-NxK2abc",
            onGroupIdChange = {},
            onConfirm = {},
            onDismiss = {},
            isLoading = false
        )
    }
}

@Preview(name = "Join Group Dialog — Loading")
@Composable
private fun PreviewJoinGroupDialogLoading() {
    PinPointTheme {
        JoinGroupDialogComposable(
            groupId = "-NxK2abc",
            onGroupIdChange = {},
            onConfirm = {},
            onDismiss = {},
            isLoading = true
        )
    }
}

@Preview(name = "Join Group Dialog — Empty ID")
@Composable
private fun PreviewJoinGroupDialogEmpty() {
    PinPointTheme {
        JoinGroupDialogComposable(
            groupId = "",
            onGroupIdChange = {},
            onConfirm = {},
            onDismiss = {},
            isLoading = false
        )
    }
}
