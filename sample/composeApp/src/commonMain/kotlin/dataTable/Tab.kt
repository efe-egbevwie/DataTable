package dataTable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dataTable.samples.pxToDp

@Composable
fun TabRow(
    selectedItemIndex: Int,
    itemCount: Int,
    indicatorColor: Color,
    tabItem: @Composable (isSelected: Boolean, tabIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabWidths = remember { mutableStateMapOf<Int, Int>() }
    val indicatorWidthPx by remember(selectedItemIndex, tabWidths.toMap()) {
        mutableIntStateOf(tabWidths[selectedItemIndex] ?: 0)
    }
    val indicatorOffsetPx by remember(selectedItemIndex, tabWidths.toMap()) {
        val precedingWidths = tabWidths
            .filterKeys { it < selectedItemIndex }
            .values
            .sum()
        mutableIntStateOf(precedingWidths)
    }
    val animatedIndicatorWidth: Dp by animateDpAsState(
        targetValue = indicatorWidthPx.pxToDp(),
        animationSpec = tween(easing = LinearEasing),
        label = "indicatorWidthAnimation"
    )

    val animatedIndicatorOffset: Dp by animateDpAsState(
        targetValue = indicatorOffsetPx.pxToDp(),
        animationSpec = tween(easing = LinearEasing),
        label = "indicatorOffsetAnimation"
    )

    Box(modifier = modifier.height(IntrinsicSize.Max)) {
        Box(modifier = modifier.padding(6.dp)) {

            TabIndicator(
                indicatorWidth = animatedIndicatorWidth,
                indicatorOffset = animatedIndicatorOffset,
                indicatorColor = indicatorColor,
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
            ) {
                repeat(itemCount){index ->
                    val isSelected = index == selectedItemIndex
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { tabWidths[index] = it.size.width }) {
                        tabItem(isSelected, index)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 2.dp)
            .width(width = indicatorWidth)
            .offset(x = indicatorOffset)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = indicatorColor)
    )
}

