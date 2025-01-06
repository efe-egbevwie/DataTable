## DataTable

A compose multiplatform library for rendering data in tables

### Dependency

```
repositories {  
    mavenCentral()
}
```

```
implementation("io.github.efe-egbevwie:dataTable:0.0.2")
```

### Usage

```kotlin
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
```

<br>
<br>

### Android

<img alt="Android" title="Android" src=media/dataTable_android.gif width="70%" height="70%">

<br>
<br>

### Desktop

<img alt="Desktop" title="Desktop" src=media/dataTable_desktop.gif width="600" height="330">

### Web(Wasm)

<img alt="Web" title="Web" src="media/dataTable_web.gif">

