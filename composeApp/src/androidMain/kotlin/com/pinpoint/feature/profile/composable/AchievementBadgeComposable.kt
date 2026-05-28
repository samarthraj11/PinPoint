package com.pinpoint.feature.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors

@Composable
fun AchievementBadgeComposable(emoji: String, label: String, unlocked: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(
                    width = 2.dp,
                    color = if (unlocked) LocalColors.Primary else LocalColors.BorderMedium,
                    shape = CircleShape
                )
                .background(
                    if (unlocked) LocalColors.PrimaryLight else LocalColors.PrimaryDim,
                    CircleShape
                )
                .then(if (!unlocked) Modifier else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp,
                modifier = if (!unlocked) Modifier then Modifier else Modifier
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (unlocked) LocalColors.TextPrimary else LocalColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp,
            letterSpacing = (-0.3).sp
        )
    }
}
