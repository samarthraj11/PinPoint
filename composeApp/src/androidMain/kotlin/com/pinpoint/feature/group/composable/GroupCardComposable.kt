package com.pinpoint.feature.group.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.domain.model.Group

@Composable
fun GroupCardComposable(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.SurfaceDark
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalColors.PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name.take(2).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${group.memberCount} member${if (group.memberCount != 1) "s" else ""}",
                    fontSize = 13.sp,
                    color = LocalColors.TextSecondary
                )
            }

            // Member avatars stack
            Row(modifier = Modifier.padding(end = 8.dp)) {
                val displayMembers = group.members.values.take(3)
                displayMembers.forEachIndexed { index, member ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-8 * index).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when (index) {
                                    0 -> LocalColors.Primary
                                    1 -> LocalColors.Orange300
                                    else -> LocalColors.Orange200
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.displayName.take(1).uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalColors.White
                        )
                    }
                }
                if (group.memberCount > 3) {
                    Box(
                        modifier = Modifier
                            .offset(x = (-8 * displayMembers.size).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(LocalColors.SurfaceDarkElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${group.memberCount - 3}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalColors.TextSecondary
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open group",
                tint = LocalColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
