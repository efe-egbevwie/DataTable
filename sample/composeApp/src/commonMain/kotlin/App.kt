import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dataTable.sample.DataTableSample
import dataTable.sample.gameStatsList

@Composable
fun App() {
    MaterialTheme {
        DataTableSample(gameStats = gameStatsList, modifier = Modifier.padding(10.dp))
    }
}




