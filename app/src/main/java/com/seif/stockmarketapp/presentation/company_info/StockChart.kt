package com.seif.stockmarketapp.presentation.company_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seif.stockmarketapp.domain.model.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    infos: List<IntraDayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 100f // values in pixels
    val transparentGraphColor =
        remember { // use remember to make sure that we calculate this values every time we recomposition
            graphColor.copy(alpha = 0.5f)
        }
    val upperValue = remember(infos) { // recalculated when infos is changing
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt()
            ?: 0 // added one on the upper value then we round it (floor rounding: rounded to the lower number)
    }
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }

    val density = LocalDensity.current
    val textPaint = remember(density) { // whenever density changes we recalculate this paint object
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Canvas(modifier = modifier) {
        val spacePerHour =
            (size.width - spacing) / infos.size // value in pixels of space between each hour
        for (i in 0 until infos.size - 1 step 2) {
            val info = infos[i]
            val hour = info.date.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour, // to handle distance between hours
                    size.height - 5,
                    textPaint
                )
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (0..5).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }
        // actual graph
        var lastX = 0f
        val strokePath = Path().apply {
            // we need to transform the close values from the csv table to canvas coordinates
            val height = size.height
            for (i in infos.indices) {
                val info: IntraDayInfo = infos[i]
                val nextInfo: IntraDayInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio: Double = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightValue: Double = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightValue * height).toFloat()

                if (i == 0) {
                    moveTo( // first point of teh graph
                        x1,
                        y1
                    )
                }
                lastX = (x1 + x2) / 2f
                // drawing line
                quadraticBezierTo( // these are the mathmatic calculations with make a smooth path
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }
        val fillPath =
            android.graphics.Path(strokePath.asAndroidPath()).asComposePath() // to copy  this path
                .apply {
                    lineTo(lastX, size.height - spacing) // down line
                    lineTo(spacing, size.height - spacing) // fromLeftToRight line
                    close() // connect the current coordinate of the path with the starting point
                }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing  // where we want the gradient ot stop
            )
        )
        drawPath(
            strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }

}