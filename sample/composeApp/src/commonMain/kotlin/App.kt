import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dataTable.sample.DataTableSample
import dataTable.sample.gameStatsList

@Composable
fun App() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .width(800.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            DataTableSample(
                gameStats = gameStatsList,
                modifier = Modifier
                    .height(400.dp)
                    .padding(10.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            )
        }
    }
}




