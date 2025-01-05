package com.efe.dataTable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * Creates a DataTable with sticky header and scrollable content.
 *
 * @param tableModifier Modifier for the entire DataTable.
 * @param verticalLazyListState ScrollState for the vertical scroll state.
 * @param horizontalScrollState ScrollState for the horizontal scroll state.
 * @param columnCount Number of columns in the DataTable.
 * @param rowCount Number of rows in the DataTable, should correspond to the number of items in your list.
 * @param headerBackgroundColor Background Color for the table header row
 * @param tableBackgroundColor Background color for all rows excluding the header roq
 * @param tableHeaderContent composable to define the content of the table header cell.
 * @param divider Optional divider composable. Provides the max width of all the rows for uniformity
 * @param cellContent composable to define the content of each data cell.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataTable(
    tableModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    headerBackgroundColor: Color,
    tableBackgroundColor: Color = Color.Unspecified,
    tableHeaderContent: @Composable (columnIndex: Int) -> Unit,
    divider: @Composable (rowWidth: Dp) -> Unit = {},
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit,
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
    Box(modifier = tableModifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        var rowWidth by remember { mutableStateOf(0) }

        LazyColumn(state = verticalLazyListState) {
            // Sticky header renders a row that contains the content for the table header
            // the number of items in this row corresponds to the columnCount parameter.
            stickyHeader {
                Column(modifier = Modifier.background(headerBackgroundColor)) {
                    TableRow(
                        rowIndex = 0,
                        isHeader = true,
                        columnCount = columnCount,
                        backgroundColor = headerBackgroundColor,
                        columnWidths = columnWidths,
                        tableHeaderContent = tableHeaderContent,
                        tableCellContent = cellContent,
                        rowWidth = {}
                    )
                }
            }

            items(rowCount) { rowIndex ->
                Column (modifier = Modifier.background(tableBackgroundColor)){
                    TableRow(
                        rowIndex,
                        isHeader = false,
                        columnCount = columnCount,
                        backgroundColor = tableBackgroundColor,
                        columnWidths = columnWidths,
                        tableHeaderContent = tableHeaderContent,
                        tableCellContent = cellContent,
                        rowWidth = {
                            rowWidth = it
                        }
                    )
                    divider(rowWidth.pxToDp())
                }
            }
        }
    }
}


/**
 * Private helper function to handle rendering of a data row for either the header or the content.
 * each row renders tableHeaderContent or tableCellContent items for the number of columnCount
 *
 * @param rowIndex Index of the row to be rendered.
 * @param isHeader: Determines if a header row or content row should be rendered
 * @param columnCount: The number of columns for the data
 * @param backgroundColor: The background color for each row
 * @param columnWidths: stores the width of each column with the column index as the key
 * @param tableHeaderContent: composable for each cell in the table header
 * @param tableCellContent: composable for each cell in the table rows excluding the header
 * @param tableCellContent Lambda function to define the content of each cell in the row.
 * @param rowWidth: Returns the max width of the row
 */
@Composable
private fun TableRow(
    rowIndex: Int,
    isHeader: Boolean = false,
    columnCount: Int,
    backgroundColor: Color,
    columnWidths: SnapshotStateMap<Int, Int>,
    tableHeaderContent: @Composable (columnIndex: Int) -> Unit,
    tableCellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit,
    rowWidth: (Int) -> Unit,
) {
    val rowModifier = Modifier
        .background(color = backgroundColor)
        .onGloballyPositioned { rowWidth(it.size.width) }
    Row(modifier = if (isHeader) Modifier else rowModifier) {
        (0 until columnCount).forEach { columnIndex ->
            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)

                        val existingWidth = columnWidths[columnIndex] ?: 0
                        val maxWidth = maxOf(existingWidth, placeable.width)

                        if (maxWidth > existingWidth) {
                            columnWidths[columnIndex] = maxWidth
                        }

                        layout(width = maxWidth, height = placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
            ) {
                if (isHeader) {
                    tableHeaderContent(columnIndex)
                } else {
                    tableCellContent(columnIndex, rowIndex)
                }
            }
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }