package com.pinpoint.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tutorlog.design.LocalColors

@Composable
fun BottomBarComposable(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
            .background(LocalColors.BackgroundDark)
            .padding(vertical = 12.dp)
            .padding(horizontal = 24.dp)
    ) {

    }
}

@Preview
@Composable
private fun PreviewBottomBarComposable() {
    BottomBarComposable()
}