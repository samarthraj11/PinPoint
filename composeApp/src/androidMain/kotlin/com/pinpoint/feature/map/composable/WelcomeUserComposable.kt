package com.pinpoint.feature.map.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tutorlog.design.LocalColors

@Composable
fun WelcomeUserComposable(
    displayName: String,
    photoUrl: String?,
    groupId: String,
    memberCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LocalColors.BackgroundDark.copy(alpha = 0.9f), shape = RoundedCornerShape(12.dp))
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Profile picture of $displayName",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LocalColors.Primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Welcome",
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = LocalColors.TextSecondary
                )
                Text(
                    text = displayName.ifEmpty { "User" },
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LocalColors.Primary, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Group $groupId",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "$memberCount member${if (memberCount != 1) "s" else ""}",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWelcomeUserComposable() {
    WelcomeUserComposable(
        displayName = "Samarth",
        photoUrl = null,
        groupId = "A1B2C3",
        memberCount = 5,
        modifier = Modifier.padding(16.dp)
    )
}
