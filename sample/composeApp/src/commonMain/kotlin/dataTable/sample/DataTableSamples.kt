package dataTable.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.efe.dataTable.DataTable
import com.efe.dataTable.sample.PlayerStats

@Composable
fun DataTableSample(gameStats: List<PlayerStats>, modifier: Modifier = Modifier) {
    DataTable(
        tableModifier = modifier,
        headerRowModifier = Modifier
            .background(color = MaterialTheme.colors.secondary),
        rowModifier = Modifier.border(1.dp, color = MaterialTheme.colors.onSurface),
        verticalLazyListState = rememberLazyListState(),
        horizontalScrollState = rememberScrollState(),
        columnCount = 5,
        rowCount = gameStats.size,
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.End
                )
            }
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
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Start
            )
        }
    )
}