package com.pinpoint.feature.map.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.domain.model.Group

@Composable
fun GroupsListContentComposable(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
    ) {
        // Header
        Text(
            text = "Your Groups",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Text(
            text = "Tap a group to view members and share your location",
            fontSize = 14.sp,
            color = LocalColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(groups, key = { it.id }) { group ->
                GroupQuickCardComposable(group = group, onClick = { onGroupClick(group) })
            }
        }
    }
}
