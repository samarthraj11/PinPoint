package com.pinpoint.feature.login.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.R

@Composable
fun LoginComposable(
    onGoogleSignInClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = LocalColors.Gray900
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo Section
                LogoSection()

                Spacer(modifier = Modifier.height(40.dp))

                // Title Section
                TitleSection()

                Spacer(modifier = Modifier.height(32.dp))

                // Sign In Button
                GoogleSignInButton(onClick = onGoogleSignInClick, isLoading = isLoading)


                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "By signing in, you agree to our terms and privacy policy.",
                    fontSize = 14.sp,
                    color = LocalColors.Gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .shadow(
                elevation = 24.dp,
                shape = CircleShape
            )
            .background(
                color = LocalColors.Gray800,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ballooon),
            contentDescription = "Music Note",
            modifier = Modifier.size(60.dp),
            tint = LocalColors.PrimaryGreen
        )
    }
}

@Composable
private fun TitleSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Welcome Back",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sign in to manage your classes",
            fontSize = 14.sp,
            color = LocalColors.Gray400
        )
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = LocalColors.Gray900,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ballooon),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Sign in with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginComposable(
        onGoogleSignInClick = {},
        isLoading = true,
    )
}


