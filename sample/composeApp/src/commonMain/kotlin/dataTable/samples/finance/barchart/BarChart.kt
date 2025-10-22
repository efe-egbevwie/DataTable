package dataTable.samples.finance.barchart

/*
 * Designed and developed by 2024 androidpoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * A customizable animated bar chart component for Jetpack Compose.
 *
 * @param renderer The data renderer that provides chart data and drawing logic
 * @param modifier Modifier for customizing the chart's layout
 * @param animate Whether to animate the chart on initial display (default: true)
 */
@Composable
fun BarChart(
    renderer: BarChartDataRenderer,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
) {
    val animationProgress =
        remember {
            Animatable(if (animate) 0f else 1f)
        }
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(animate) {
        if (animate) {
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec =
                    tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing,
                    ),
            )
        }
    }

    val labels = renderer.getLabels()
    val barsPerGroup = renderer.barsPerGroup()

    Canvas(
        modifier =
            modifier
                .hoverable(interactionSource),
    ) {
        if (size.width < 1f || size.height < 1f) return@Canvas
        val chartDimensions = calculateChartDimensions(size.width, size.height)
        val (_, chartHeight, chartWidth, _, chartBottom, chartLeft) = chartDimensions
        val maxValue = renderer.calculateMaxValue()
        val (barWidth, groupSpacing) =
            calculateBarDimensions(
                chartWidth = chartWidth,
                dataSize = labels.size,
                barsPerGroup = barsPerGroup,
            )
        drawBars(
            renderer = renderer,
            labels = labels,
            chartLeft = chartLeft,
            chartBottom = chartBottom,
            chartHeight = chartHeight,
            barWidth = barWidth,
            groupSpacing = groupSpacing,
            barsPerGroup = barsPerGroup,
            maxValue = maxValue,
            animationProgress = animationProgress.value,
        )

    }
}

/**
 * Calculates the dimensions for the chart layout.
 * @return Triple of (padding, height, width, top, bottom, left)
 */
private fun calculateChartDimensions(
    width: Float,
    height: Float,
): ChartDimensions {
    val padding = width * 0.15f
    return ChartDimensions(
        chartPadding = padding,
        chartHeight = height * 0.7f,
        chartWidth = width - (padding * 2),
        chartTop = height * 0.15f,
        chartBottom = height * 0.15f + (height * 0.7f),
        chartLeft = padding,
    )
}

/**
 * Calculates the width of bars and spacing between groups.
 * @return Pair of (barWidth, groupSpacing)
 */
private fun calculateBarDimensions(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
): Pair<Float, Float> {
    if (dataSize <= 0) return Pair(0f, 0f)

    val totalGroups = dataSize
    val totalSpacing = chartWidth * 0.2f
    val spacing = totalSpacing / (totalGroups + 1)
    val availableWidth = chartWidth - totalSpacing
    val barWidth = (availableWidth / (totalGroups * barsPerGroup))

    return Pair(barWidth, spacing)
}

/**
 * Draws the bars for each data point with proper spacing and animation.
 */
private fun DrawScope.drawBars(
    renderer: BarChartDataRenderer,
    labels: List<String>,
    chartLeft: Float,
    chartBottom: Float,
    chartHeight: Float,
    barWidth: Float,
    groupSpacing: Float,
    barsPerGroup: Int,
    maxValue: Float,
    animationProgress: Float,
) {
    var currentLeft = chartLeft
    labels.forEachIndexed { index, _ ->
        renderer.drawBars(
            drawScope = this,
            index = index,
            left = currentLeft,
            barWidth = barWidth,
            groupSpacing = groupSpacing,
            chartBottom = chartBottom,
            chartHeight = chartHeight,
            maxValue = maxValue,
            animationProgress = animationProgress,
        )

        val groupWidth = barWidth * barsPerGroup
        currentLeft += groupWidth + groupSpacing
    }
}

/**
 * Data class to hold chart dimension calculations
 */
private data class ChartDimensions(
    val chartPadding: Float,
    val chartHeight: Float,
    val chartWidth: Float,
    val chartTop: Float,
    val chartBottom: Float,
    val chartLeft: Float,
)
