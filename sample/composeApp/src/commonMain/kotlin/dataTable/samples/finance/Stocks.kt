package dataTable.samples.finance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efe.dataTable.DataTable
import com.efe.dataTable.DefaultColumnDivider
import com.efe.dataTable.DefaultItemDivider
import com.efe.dataTable.Res
import com.efe.dataTable.allDrawableResources
import dataTable.samples.finance.barchart.BarChart
import dataTable.samples.finance.barchart.BarChartRenderer
import dataTable.samples.finance.barchart.SimpleBarChartData
import dataTable.samples.finance.barchart.portFolioJson
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Stocks(modifier: Modifier = Modifier) {
    var portfolio: Portfolio by remember {
        mutableStateOf(getPortfolio())
    }

    LaunchedEffect(key1 = portfolio) {
        delay(60.milliseconds)
        val newPortfolioItems = portfolio.items.map {
            randomizePortfolio(it)
        }
        portfolio = portfolio.copy(items = newPortfolioItems)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StocksTable(
                stocks = portfolio.items,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun StocksTable(
    stocks: List<PortfolioItem>,
    modifier: Modifier = Modifier
) {
    DataTable(
        modifier = modifier,
        headerBackgroundColor = MaterialTheme.colorScheme.primary,
        tableBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
        columnCount = 5,
        rowCount = stocks.size,
        columnDivider = {
            DefaultColumnDivider(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxHeight()
            )
        },
        itemDivider = {
            DefaultItemDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        tableHeaderContent = { columnIndex: Int ->
            val columnText = when (columnIndex) {
                0 -> "Ticker"
                1 -> "TimeLine"
                2 -> "Instrument"
                3 -> "P&L"
                4 -> "Total Value"
                else -> ""
            }

            val textAlign = when (columnIndex) {
                0, 1 -> TextAlign.Start
                else -> TextAlign.End
            }

            Text(
                text = columnText,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                textAlign = textAlign
            )
        },
        onRowClicked = null,
        cellContent = { columnIndex: Int, rowIndex: Int ->
            val stock = stocks[rowIndex]
            val pAndL = (stock.price / stock.purchasePrice) * stock.quantity
            val instrument = stock.instrument
            val totalValue = stock.quantity * stock.price

            val cellText = when (columnIndex) {
                2 -> instrument
                3 -> pAndL.roundDecimalPlaces().toString().appendDollarSign()
                4 -> totalValue.roundDecimalPlaces().toString().appendDollarSign()
                else -> ""
            }
            val textPaddingBottomPadding = 6.dp
            val textAlign = when (columnIndex) {
                0, 1 -> TextAlign.Start
                else -> TextAlign.End
            }

            when (columnIndex) {
                0 -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = textPaddingBottomPadding, start = 8.dp)
                    ) {
                        val imageRes = Res.allDrawableResources.toList()
                            .find { (resourceName, _) -> resourceName == stock.ticker }?.second

                        imageRes?.let {
                            Image(
                                painter = painterResource(resource = imageRes),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                                    .clip(shape = CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        SelectionContainer {
                            Text(
                                text = stock.name,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }

                }

                1 -> {
                    val barChartData = SimpleBarChartData(
                        labelsList = List(stock.timeline.size) { "" },
                        values = stock.timeline.map { it.toFloat() },
                        colors = List(stock.timeline.size) { MaterialTheme.colorScheme.onPrimaryContainer }
                    )

                    val renderer = BarChartRenderer(data = barChartData)
                    BarChart(
                        renderer = renderer,
                        animate = true,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }

                2, 3, 4 -> {
                        SelectionContainer(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = textPaddingBottomPadding)

                        ) {
                            Text(
                                text = cellText,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .padding(horizontal = 8.dp),
                                textAlign = textAlign
                            )
                        }

                }
            }
        }
    )
}

private fun getPortfolio(): Portfolio {
    val json = Json { prettyPrint = true }
    return json.decodeFromString<Portfolio>(portFolioJson)
}


private const val PERCENTAGE_CHANGE = 5.0
private fun randomizePortfolio(item: PortfolioItem): PortfolioItem {
    val isRandomChance = Random.nextDouble() < 0.1
    if (!isRandomChance) {
        return item
    }
    val rnd = (Random.nextDouble() * PERCENTAGE_CHANGE) / 100.0
    val factor = if (Random.nextDouble() > 0.5) 1.0 - rnd else 1.0 + rnd
    val newPrice = if (item.price < 10) item.price * factor else Random.nextDouble() * 40 + 10
    val finalPrice = round(newPrice * 100) / 100.0

    val newTimeline = item.timeline
        .drop(1)
        .plus(finalPrice)
    return item.copy(
        price = finalPrice.toFloat(),
        timeline = newTimeline
    )
}


private fun Float.roundDecimalPlaces(decimalPlaces: Int = 3): Double {
    return round(this * 10.0.pow(decimalPlaces)) / 10.0.pow(decimalPlaces)
}

private fun String.appendDollarSign(): String = "$$this"