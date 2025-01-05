package dataTable.sample

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.efe.dataTable.DataTable

@Composable
fun DataTableSample(gameStats: List<PlayerStats>, modifier: Modifier = Modifier) {
    DataTable(
        tableModifier = modifier,
        headerBackgroundColor = MaterialTheme.colors.primaryVariant,
        tableBackgroundColor = MaterialTheme.colors.background,
        verticalLazyListState = rememberLazyListState(),
        horizontalScrollState = rememberScrollState(),
        columnCount = 5,
        rowCount = gameStats.size,
        divider = { rowWidth ->
            Divider(
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(rowWidth).padding(top = 8.dp, bottom = 8.dp)
            )
        },
        tableHeaderContent = { columnIndex: Int ->
            val columnText = when (columnIndex) {
                0 -> "Player"
                1 -> "Minutes played"
                2 -> "Points"
                3 -> "Rebounds"
                4 -> "Assists"
                else -> ""
            }

            Text(
                text = columnText,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Start
            )
        },
        cellContent = { columnIndex: Int, rowIndex: Int ->
            val cellText = when (columnIndex) {
                0 -> gameStats[rowIndex].playerName
                1 -> gameStats[rowIndex].minutesPlayed.toString()
                2 -> gameStats[rowIndex].points.toString()
                3 -> gameStats[rowIndex].rebounds.toString()
                4 -> gameStats[rowIndex].assists.toString()
                else -> ""
            }

            SelectionContainer {
                Text(
                    text = cellText,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    )
}