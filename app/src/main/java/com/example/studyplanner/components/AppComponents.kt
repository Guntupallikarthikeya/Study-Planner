package com.example.studyplanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AccentBlue = Color(0xFF7C9CFF)

val ACCENT_BLUE = Color(0xFF7C9CFF)
val ACCENT_PURPLE = Color(0xFFB388FF)

fun appBackground(isDarkMode: Boolean): Brush {
    return if (isDarkMode) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF050816),
                Color(0xFF0D1636),
                Color(0xFF17245A)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFF7F8FF),
                Color(0xFFE9EDFF),
                Color(0xFFDDE5FF)
            )
        )
    }
}

fun appText(isDarkMode: Boolean): Color {
    return if (isDarkMode) Color(0xFFF7F8FF) else Color(0xFF111827)
}

fun appMuted(isDarkMode: Boolean): Color {
    return if (isDarkMode) Color(0xFFB9C3E6) else Color(0xFF4B5563)
}

fun cardColor(isDarkMode: Boolean): Color {
    return if (isDarkMode)
        Color(0xFF243063).copy(alpha = 0.9f)
    else
        Color.White.copy(alpha = 0.95f)
}

@Composable
fun GradientScreen(
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.background(appBackground(isDarkMode))) {
        content()
    }
}

@Composable
fun GlassCard(
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .border(
                1.dp,
                Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(isDarkMode)),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        content()
    }
}

@Composable
fun PrimaryAppButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentBlue,
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}

@Composable
fun SecondaryAppButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AccentBlue
        )
    ) {
        Text(text)
    }
}

@Composable
fun PremiumTextField(
    value: String,
    label: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        shape = RoundedCornerShape(18.dp),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = appText(isDarkMode),
            unfocusedTextColor = appText(isDarkMode),
            focusedLabelColor = AccentBlue,
            unfocusedLabelColor = appMuted(isDarkMode),
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = appMuted(isDarkMode),
            cursorColor = AccentBlue
        )
    )
}