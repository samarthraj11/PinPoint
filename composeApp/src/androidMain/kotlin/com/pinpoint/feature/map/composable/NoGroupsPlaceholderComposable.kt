package com.pinpoint.feature.map.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.PinPointTheme

@Composable
fun NoGroupsPlaceholderComposable(
    onCreateGroup: () -> Unit,
    onJoinGroup: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(LocalColors.PrimaryDim),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83D\uDCCD", // pin emoji
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "No Groups Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Join a group or create a new one to start sharing your location with friends and family.",
            fontSize = 15.sp,
            color = LocalColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onCreateGroup,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalColors.Primary
            )
        ) {
            Text(
                text = "Create a Group",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = LocalColors.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onJoinGroup,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LocalColors.Primary
            ),
            border = ButtonDefaults.outlinedButtonBorder(true).copy(
                brush = androidx.compose.ui.graphics.SolidColor(LocalColors.Primary)
            )
        ) {
            Text(
                text = "Join Existing Group",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, name = "No Groups Placeholder")
@Composable
private fun PreviewNoGroupsPlaceholder() {
    PinPointTheme {
        NoGroupsPlaceholderComposable(
            onCreateGroup = {},
            onJoinGroup = {}
        )
    }
}
