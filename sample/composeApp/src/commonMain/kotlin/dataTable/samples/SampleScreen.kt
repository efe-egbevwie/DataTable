package dataTable.samples

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dataTable.TabRow
import dataTable.samples.boxScore.BoxScore
import dataTable.samples.finance.Stocks
import dataTable.theme.SampleTheme
import kotlinx.coroutines.launch

private val examples = listOf("PortFolio", "Box Score")

@Composable
fun SampleScreen() {
    SampleTheme {
        Surface {
            BoxWithConstraints {
                val pagerState = rememberPagerState(pageCount = { examples.size })
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                ) {
                    var selectedTab by rememberSaveable {
                        mutableIntStateOf(0)
                    }

                    Text(
                        text = "DataTable Examples",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .requiredWidth(this@BoxWithConstraints.maxWidth)
                            .alpha(0.3f)
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    TabRow(
                        selectedItemIndex = selectedTab,
                        itemCount = examples.size,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primary),
                        tabItem = { isSelected, index ->
                            Text(
                                text = examples[index],
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        selectedTab = index
                                        scope.launch {
                                            pagerState.scrollToPage(index)
                                        }
                                    }
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    HorizontalPager(state = pagerState, userScrollEnabled = false) { pageIndex ->
                        when (pageIndex) {
                            0 -> Stocks()
                            1 -> BoxScore()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }