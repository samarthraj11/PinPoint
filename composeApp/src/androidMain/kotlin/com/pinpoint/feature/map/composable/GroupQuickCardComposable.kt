package com.pinpoint.feature.map.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.PinPointTheme
import com.pinpoint.domain.model.Group

@Composable
fun GroupQuickCardComposable(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColors.SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalColors.PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name.take(2).uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${group.memberCount} member${if (group.memberCount != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = LocalColors.TextSecondary
                )
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

@Preview(showBackground = true, name = "Group Quick Card")
@Composable
private fun PreviewGroupQuickCard() {
    PinPointTheme {
        GroupQuickCardComposable(
            group = Group(
                id = "-NxK2abc",
                name = "Weekend Trip",
                createdBy = "uid_abc123",
                createdByName = "Samarth",
                createdAt = 0L,
                memberCount = 4
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Group Quick Card — Single Member")
@Composable
private fun PreviewGroupQuickCardSingleMember() {
    PinPointTheme {
        GroupQuickCardComposable(
            group = Group(
                id = "-NxK2xyz",
                name = "Solo",
                createdBy = "uid_abc123",
                createdByName = "Samarth",
                createdAt = 0L,
                memberCount = 1
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Group Quick Card — Long Name")
@Composable
private fun PreviewGroupQuickCardLongName() {
    PinPointTheme {
        GroupQuickCardComposable(
            group = Group(
                id = "-NxK2long",
                name = "This Is A Very Long Group Name That Should Ellipsize",
                createdBy = "uid_abc123",
                createdByName = "Samarth",
                createdAt = 0L,
                memberCount = 12
            ),
            onClick = {}
        )
    }
}
