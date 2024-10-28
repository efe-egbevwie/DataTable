package com.efe.dataTable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout


/**
 * Creates a DataTable with sticky header and scrollable content.
 *
 * @param tableModifier Modifier for the entire DataTable.
 * @param rowModifier Modifier for individual data rows.
 * @param headerRowModifier Modifier for the header row.
 * @param verticalLazyListState LazyListState for the vertical scroll state.
 * @param horizontalScrollState ScrollState for the horizontal scroll state.
 * @param columnCount Number of columns in the DataTable.
 * @param rowCount Number of rows in the DataTable, should correspond to the number of items in your list.
 * @param beforeRow composable to render before rendering each data row.
 * @param afterRow composable to render after rendering each data row.
 * @param tableHeaderContent composable to define the content of the table header cell.
 * @param cellContent composable to define the content of each data cell.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataTable(
    tableModifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    headerRowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    tableHeaderContent: @Composable (columnIndex: Int) -> Unit,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit,
) {
    // Mutable state to store column widths for proper cell layout
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    // Box containing the entire DataTable with horizontal scrolling
    Box(modifier = tableModifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        LazyColumn(state = verticalLazyListState) {
            // Sticky header renders a row that contains the content for the table header
            // the number of items in this row corresponds to the columnCount parameter.
            stickyHeader {
                TableRow(
                    rowIndex = 0,
                    isHeader = true,
                    headerRowModifier = headerRowModifier,
                    columnCount = columnCount,
                    beforeRow = beforeRow,
                    afterRow = afterRow,
                    tableRowModifier = rowModifier,
                    columnWidths = columnWidths,
                    tableCellContent = cellContent,
                    tableHeaderContent = tableHeaderContent
                )
            }

            items(rowCount) { rowIndex ->
                TableRow(
                    rowIndex,
                    isHeader = false,
                    headerRowModifier = headerRowModifier,
                    columnCount = columnCount,
                    beforeRow = beforeRow,
                    afterRow = afterRow,
                    tableRowModifier = rowModifier,
                    columnWidths = columnWidths,
                    tableCellContent = cellContent,
                    tableHeaderContent = tableHeaderContent
                )
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
 * @param headerRowModifier: Modifier for individual data rows in the table header.
 * @param tableRowModifier: Modifier for individual data rows in the table content cells
 * @param columnCount: The number of columns for the data
 * @param beforeRow: optional composable to render before each row
 * @param afterRow: optional composable to render after each row
 * @param columnWidths: stores the width of each column with the column index as the key
 * @param tableHeaderContent: composable for each cell in the table header
 * @param tableCellContent: composable for each cell in the table rows excluding the header
 * @param tableCellContent Lambda function to define the content of each cell in the row.
 */
@Composable
private fun TableRow(
    rowIndex: Int,
    isHeader: Boolean = false,
    headerRowModifier: Modifier,
    tableRowModifier: Modifier,
    columnCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    columnWidths: SnapshotStateMap<Int, Int>,
    tableHeaderContent: @Composable (columnIndex: Int) -> Unit,
    tableCellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit,
) {
    Surface {
        Column(modifier = if (isHeader) headerRowModifier else tableRowModifier) {
            beforeRow?.invoke(rowIndex)
            Row {
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
            afterRow?.invoke(rowIndex)
        }
    }
}