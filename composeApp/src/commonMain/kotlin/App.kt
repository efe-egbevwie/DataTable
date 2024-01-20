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
import com.efe.dataTable.PlayerStats
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        DataTableSample(gameStats = gameStatsList, modifier = Modifier.padding(10.dp))
//        var showContent by remember { mutableStateOf(false) }
//        val greeting = remember { Greeting().greet() }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource("compose-multiplatform.xml"), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}


val gameStatsList = buildList {
    add(
        PlayerStats(
            playerId = 1,
            playerName = "Keldon Johnson ",
            minutesPlayed = 29,
            points = 25,
            rebounds = 4,
            assists = 3
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Julian Champagnie",
            minutesPlayed = 19,
            points = 9,
            rebounds = 2,
            assists = 3
        )
    )

    add(
        PlayerStats(
            playerId = 3,
            playerName = "Zach Collins",
            minutesPlayed = 26,
            points = 16,
            rebounds = 4,
            assists = 4
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Devin Vassell",
            minutesPlayed = 36,
            points = 17,
            rebounds = 5,
            assists = 7
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Tre Jones",
            minutesPlayed = 32,
            points = 16,
            rebounds = 4,
            assists = 2
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Jeremy Sochan",
            minutesPlayed = 34,
            points = 11,
            rebounds = 8,
            assists = 8
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Dominic Barlow",
            minutesPlayed = 19,
            points = 12,
            rebounds = 4,
            assists = 2
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Blake Wesley",
            minutesPlayed = 15,
            points = 9,
            rebounds = 2,
            assists = 1
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Cedi Osman",
            minutesPlayed = 13,
            points = 5,
            rebounds = 3,
            assists = 1
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Julian Champagnie",
            minutesPlayed = 19,
            points = 9,
            rebounds = 2,
            assists = 3
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Doug McDermott",
            minutesPlayed = 9,
            points = 0,
            rebounds = 0,
            assists = 2
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Malaki Branham",
            minutesPlayed = 8,
            points = 0,
            rebounds = 1,
            assists = 0
        )
    )


}

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
                textAlign = TextAlign.Start,
//                style = MaterialTheme.typography.h1
            )
        }
    )
}