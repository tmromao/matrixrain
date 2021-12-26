package com.example.matrixrain


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*


import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.example.matrixrain.characters
import kotlinx.coroutines.delay
import kotlin.random.Random


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.Text


import androidx.compose.ui.graphics.Color

val characters = listOf(
    "a",
    "b",
    "c",
    "e",
    "f",
    "g",
    "h",
    "i",
    "j",
    "k",
    "l",
    "m",
    "n",
    "o",
    "p",
    "q",
    "r",
    "s",
    "t",
    "u",
    "v",
    "v",
    "z"
)

@Composable
fun MatrixRain(stripCount: Int = 20, modifier: Modifier = Modifier) {
    // MatrixChar(char = characters[5], 1000)
    Row(modifier = Modifier.background(Color.Black)) {
        for (column in 0..stripCount) {
            MatrixColumn(
                yStartDelay = Random.nextInt(8) * 1000L,
                crawlSpeed = (Random.nextInt(10) * 10L) + 100
            )
        }
    }
}

@Composable
fun RowScope.MatrixColumn(crawlSpeed: Long, yStartDelay: Long) {

    BoxWithConstraints(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {

        val maxWidthDp = maxWidth
        val pxWidth = with(LocalDensity.current) {maxWidth.toPx() }
        val pxHeight = with(LocalDensity.current) {maxHeight.toPx() }

        val matrixStrip =
            remember { Array((pxHeight / pxWidth).toInt() + 1) { characters.random() } }

        var lettersToDraw = remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            for ( row in 0 until lettersToDraw.value) {
                MatrixChar(
                    textSizePx = pxWidth,
                    char = matrixStrip[row],
                    crawlSpeed = 1000,
                    onFinished = {
                        if (row >= (matrixStrip.size * 0.6).toInt()) {
                            lettersToDraw.value = 0
                        }
                    }
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(yStartDelay)
            while (true) {

                if (lettersToDraw.value <= matrixStrip.size - 1) {
                    lettersToDraw.value += 1
                }

                if (lettersToDraw.value > matrixStrip.size * 0.5) {
                    matrixStrip[Random.nextInt(lettersToDraw.value)] = characters.random()
                }
                delay(crawlSpeed)
            }
        }
    }

}

@Composable
fun MatrixChar(
    textSizePx: Float,
    char: String,
    crawlSpeed: Long,
    onFinished: () -> Unit
) {

    val startFade = remember { mutableStateOf(false) }
    val textSizePx = with(LocalDensity.current) {textSizePx.toSp() }
    var textColor = remember { mutableStateOf(Color(0xffcefbe4)) }
    val alpha = animateFloatAsState(
        targetValue = if (startFade.value) 0f else 1f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = LinearEasing
        ),
        finishedListener = { onFinished() }
    )

    Text(
        text = char,
        color = textColor.value.copy(alpha = alpha.value),
        fontSize = textSizePx
    )

    LaunchedEffect(Unit) {
        delay(crawlSpeed)
        textColor.value = Color(0xff43c728)
        startFade.value = true
    }

}



