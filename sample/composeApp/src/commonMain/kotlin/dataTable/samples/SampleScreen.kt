package dataTable.samples

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.efe.dataTable.Res
import com.efe.dataTable.github_mark
import dataTable.TabRow
import dataTable.samples.boxScore.BoxScore
import dataTable.samples.finance.Stocks
import dataTable.theme.SampleTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

private val examples = listOf("PortFolio", "Box Score")

@Composable
fun SampleScreen() {
    var isDarkTheme by remember { mutableStateOf(true) }
    SampleTheme(
        darkTheme = isDarkTheme
    ) {
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "DataTable Examples",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        ThemeSwitcher(
                            isDarkTheme = isDarkTheme,
                            onThemeChanged = { isDarkTheme = !isDarkTheme }
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        val uriHandler = LocalUriHandler.current
                        IconButton(
                            onClick = { uriHandler.openUri("https://github.com/efe-egbevwie/DataTable") }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.github_mark),
                                contentDescription = "github project link"
                            )
                        }
                    }

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
private fun ThemeSwitcher(
    isDarkTheme: Boolean,
    onThemeChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { onThemeChanged() },
            thumbContent = {
                AnimatedVisibility(
                    visible = isDarkTheme,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Icon(Icons.Filled.DarkMode, contentDescription = "Dark Theme")
                }

                AnimatedVisibility(
                    visible = !isDarkTheme,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Icon(Icons.Filled.LightMode, contentDescription = "Dark Theme")
                }
            })
    }
}


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }