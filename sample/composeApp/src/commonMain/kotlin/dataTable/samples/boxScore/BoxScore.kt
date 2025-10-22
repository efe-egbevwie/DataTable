package dataTable.samples.boxScore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efe.dataTable.DataTable
import com.efe.dataTable.DefaultColumnDivider

@Composable
fun BoxScore(modifier: Modifier = Modifier) {
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
            BoxScoreTable(
                playerStats = playerStatsList,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
            )
        }
    }
}

@Composable
private fun BoxScoreTable(
    playerStats: List<PlayerStats>,
    modifier: Modifier = Modifier
) {
    DataTable(
        modifier = modifier,
        headerBackgroundColor = MaterialTheme.colorScheme.primary,
        tableBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
        columnCount = 5,
        rowCount = playerStats.size,
        columnDivider = {
            DefaultColumnDivider(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxHeight()
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
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                textAlign = TextAlign.Start
            )
        },
        cellContent = { columnIndex: Int, rowIndex: Int ->
            val cellText = when (columnIndex) {
                0 -> playerStats[rowIndex].playerName
                1 -> playerStats[rowIndex].minutesPlayed.toString()
                2 -> playerStats[rowIndex].points.toString()
                3 -> playerStats[rowIndex].rebounds.toString()
                4 -> playerStats[rowIndex].assists.toString()
                else -> ""
            }
            SelectionContainer {
                Text(
                    text = cellText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                    textAlign = TextAlign.Start
                )
            }
        }
    )

}

data class PlayerStats(
    val playerId: Long,
    val playerName: String,
    val minutesPlayed: Int,
    val points: Int,
    val rebounds: Int,
    val assists: Int
)


val playerStatsList = buildList {
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

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Miles Bridges",
            minutesPlayed = 39,
            points = 23,
            rebounds = 6,
            assists = 3
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Brandon Miller",
            minutesPlayed = 35,
            points = 24,
            rebounds = 9,
            assists = 2
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Paul Jamaine Washington Jr",
            minutesPlayed = 28,
            points = 20,
            rebounds = 6,
            assists = 0
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Terry Rozier",
            minutesPlayed = 34,
            points = 17,
            rebounds = 6,
            assists = 8
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Lamelo Ball",
            minutesPlayed = 32,
            points = 28,
            rebounds = 1,
            assists = 8
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Cody Martin",
            minutesPlayed = 25,
            points = 5,
            rebounds = 3,
            assists = 3
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "Nathan Mensah",
            minutesPlayed = 20,
            points = 0,
            rebounds = 3,
            assists = 1
        )
    )


    add(
        PlayerStats(
            playerId = 2,
            playerName = "Nick Smith Jr.",
            minutesPlayed = 18,
            points = 5,
            rebounds = 3,
            assists = 0
        )
    )

    add(
        PlayerStats(
            playerId = 2,
            playerName = "JT Thor",
            minutesPlayed = 9,
            points = 2,
            rebounds = 2,
            assists = 0
        )
    )


}