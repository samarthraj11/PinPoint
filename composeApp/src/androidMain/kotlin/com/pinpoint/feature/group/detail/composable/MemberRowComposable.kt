package com.pinpoint.feature.group.detail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.google.android.gms.maps.model.LatLng
import com.pinpoint.domain.model.MemberLocation

@Composable
fun MemberRowComposable(
    member: MemberLocation,
    isCurrentUser: Boolean,
    myLocation: LatLng?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (isCurrentUser) LocalColors.Primary else LocalColors.SurfaceDarkElevated
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.displayName.take(1).uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) LocalColors.White else LocalColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = member.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary
                )
                if (isCurrentUser) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "You",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalColors.Primary,
                        modifier = Modifier
                            .background(
                                LocalColors.PrimaryDim,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        if (!isCurrentUser && myLocation != null) {
            val results = FloatArray(1)
            android.location.Location.distanceBetween(
                myLocation.latitude, myLocation.longitude,
                member.latitude, member.longitude,
                results
            )
            val distance = results[0]
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (distance >= 1000) {
                        String.format("%.1f km", distance / 1000)
                    } else {
                        String.format("%.0f m", distance)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
                Text(
                    text = "AWAY",
                    fontSize = 9.sp,
                    color = LocalColors.TextMuted,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
