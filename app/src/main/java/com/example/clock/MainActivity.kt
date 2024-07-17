package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockAnimation()
        }
    }
}

@Composable
fun ClockAnimation() {
    var startAngle by remember { mutableStateOf(0f) }
    var endAngle by remember { mutableStateOf(0f) }
    var color by remember { mutableStateOf(Color.Red) }

    val infiniteTransition = rememberInfiniteTransition()

    val animatedAngle = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                animatedAngle.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(durationMillis = 60000) // 60 seconds
                )
                animatedAngle.snapTo(0f)
            }
        }
    }

    val animatedColor = infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2.5f
        val secondHandLength = radius * 0.9f
        val secondHandWidth = 6f

        // Draw outer circle with changing color
        drawCircle(
            color = animatedColor.value,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 8f)
        )

        // Calculate the end point of the second hand
        val angleRad = (animatedAngle.value - 90) * (PI / 180f)
        val secondHandEndX = centerX + cos(angleRad) * secondHandLength
        val secondHandEndY = centerY + sin(angleRad) * secondHandLength

        // Draw second hand
        drawLine(
            color = Color.Red,
            start = Offset(centerX, centerY),
            end = Offset(secondHandEndX.toFloat(), secondHandEndY.toFloat()),
            strokeWidth = secondHandWidth
        )
    }
}
