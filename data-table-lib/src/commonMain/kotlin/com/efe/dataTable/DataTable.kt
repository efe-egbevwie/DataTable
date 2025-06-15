package com.efe.dataTable

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Displays data in a table format with lazy loading for rows.
 * 
 * @param defaultColumnWidth The default width for all columns if not specified individually
 * @param modifier Modifier to be applied to the table layout
 * @param verticalLazyListState State object that can be used to control and observe vertical scrolling
 * @param horizontalScrollState State object that can be used to control horizontal scrolling
 * @param columnCount The number of columns in the table
 * @param rowCount The number of rows in the table, typically the size of your list
 * @param headerBackgroundColor Background color for the header row
 * @param tableBackgroundColor Background color for the table content
 * @param tableHeaderContent Composable function that defines the content of header cells
 * @param itemDivider Optional composable that defines the divider between rows, if present, dragging the divider left or right resizes the entire column
 * @param columnDivider Optional composable that defines the divider between columns
 * @param onRowClicked Callback that is invoked when a row is clicked
 * @param onColumnClicked Callback that is invoked when a column header is clicked
 * @param cellContent Composable function that defines the content of each cell
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataTable(
    defaultColumnWidth: Dp = 180.dp,
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    headerBackgroundColor: Color = Color.Unspecified,
    tableBackgroundColor: Color = Color.Unspecified,
    itemDivider: @Composable (() -> Unit)? = {
        DefaultItemDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )
    },
    columnDivider: @Composable (() -> Unit?)? = {
        DefaultColumnDivider(
            modifier = Modifier.fillMaxHeight().padding(vertical = 8.dp),
        )
    },
    onRowClicked: ((rowIndex: Int) -> Unit) = {},
    onColumnClicked: ((columnIndex: Int) -> Unit) = {},
    tableHeaderContent: @Composable ((Int) -> Unit),
    cellContent: @Composable ((Int, Int) -> Unit),
) {

    val columnWidths = remember {
        mutableStateMapOf<Int, Dp>().apply {
            repeat(columnCount) { columnIndex ->
                this[columnIndex] = defaultColumnWidth
            }
        }
    }


    val density = LocalDensity.current.density

    val onColumnWidthChange = { columnIndex: Int, delta: Float ->
        val currentWidth = columnWidths[columnIndex] ?: defaultColumnWidth
        val newWidth = (currentWidth.value + delta / density).dp
        // Ensure column width doesn't go below a minimum value
        if (newWidth.value >= 50f) {
            columnWidths[columnIndex] = newWidth
        }
    }

    Box(
        modifier = modifier
            .horizontalScroll(horizontalScrollState)
    ) {
        var rowWidth by remember { mutableStateOf(0) }

        LazyColumn(state = verticalLazyListState) {
            // Sticky header renders a row that contains the content for the table header
            // the number of items in this row corresponds to the columnCount parameter.
            stickyHeader {
                Column(modifier = Modifier.background(color = headerBackgroundColor)) {
                    HeaderRow(
                        columnCount = columnCount,
                        defaultColumnWidth = defaultColumnWidth,
                        tableHeaderContent = tableHeaderContent,
                        columnWidths = columnWidths,
                        onColumnWidthChange = onColumnWidthChange,
                        columnDivider = columnDivider,
                        onColumnClicked = onColumnClicked
                    )
                }
            }

            items(rowCount) { rowIndex ->
                Column(modifier = Modifier.background(tableBackgroundColor)) {
                    TableRow(
                        rowIndex = rowIndex,
                        columnCount = columnCount,
                        maxColumnWidth = defaultColumnWidth,
                        tableCellContent = cellContent,
                        columnDivider = columnDivider,
                        columnWidths = columnWidths,
                        modifier = Modifier
                            .onGloballyPositioned { rowWidth = it.size.width }
                            .clickable {
                                onRowClicked(rowIndex)
                            },
                    )

                    itemDivider?.let { divider ->
                        ItemDividerContainer(
                            rowWidth = rowWidth.pxToDp(),
                        ) {
                            divider()
                        }
                    }

                }
            }
        }
    }
}


/**
 * Private helper function to handle rendering of a data row for either the header or the content.
 * each row renders tableHeaderContent or tableCellContent items for the number of columnCount
 *
 */
@Composable
private fun TableRow(
    rowIndex: Int,
    columnCount: Int,
    maxColumnWidth: Dp,
    tableCellContent: @Composable ((Int, Int) -> Unit),
    columnWidths: Map<Int, Dp>,
    columnDivider: @Composable (() -> Unit?)? = {
        DefaultColumnDivider()
    },
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.hoverable(interactionSource = remember { MutableInteractionSource() })) {
        (0 until columnCount).forEach { columnIndex ->
            Box(
                modifier = Modifier
                    .width(columnWidths[columnIndex] ?: maxColumnWidth)
            ) {
                tableCellContent(columnIndex, rowIndex)
            }

            columnDivider?.let { divider ->
                ColumnDividerContainer(
                    modifier = Modifier.height(0.dp),
                    columnIndex = columnIndex,
                    onWidthChange = { _, _ -> },
                ) {
                    divider()
                }
            }
        }
    }
}


/**
 * Private helper function to render the header row of the table.
 * 
 * This composable creates a row containing the header cells and column dividers.
 * The column dividers can be dragged to resize the columns.
 *
 * @param modifier Modifier to be applied to the header row
 * @param columnCount The number of columns in the table
 * @param defaultColumnWidth The default width for columns if not specified individually
 * @param tableHeaderContent Composable function that defines the content of header cells
 * @param columnWidths Map of column indices to their widths
 * @param onColumnWidthChange Callback that is invoked when a column width changes
 * @param columnDivider Optional composable that defines the divider between columns
 * @param onColumnClicked Callback that is invoked when a column header is clicked
 */
@Composable
private fun HeaderRow(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    columnCount: Int,
    defaultColumnWidth: Dp,
    tableHeaderContent: @Composable ((Int) -> Unit),
    columnWidths: Map<Int, Dp>,
    onColumnWidthChange: (Int, Float) -> Unit,
    columnDivider: @Composable (() -> Unit?)? = {
        DefaultColumnDivider()
    },
    onColumnClicked: ((columnIndex: Int) -> Unit) = {},
) {
    var rowHeight by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .onGloballyPositioned { rowHeight = it.size.height }
    ) {
        (0 until columnCount).forEach { columnIndex ->
            Box(
                modifier = Modifier
                    .width(columnWidths[columnIndex] ?: defaultColumnWidth)
                    .clickable {
                        onColumnClicked(columnIndex)
                    }
            ) {
                tableHeaderContent(columnIndex)
            }

            columnDivider?.let { divider ->
                ColumnDividerContainer(
                    modifier = Modifier.height(rowHeight.pxToDp()),
                    columnIndex = columnIndex,
                    onWidthChange = onColumnWidthChange,
                ) {
                    divider()
                }
            }
        }
    }
}


/**
 * Default implementation of a column divider that can be dragged to resize columns.
 * 
 * This composable renders a vertical line that serves as a visual separator between columns
 * in the LazyDataTable. When used with ColumnDividerContainer, it becomes draggable to
 * allow column resizing.
 *
 * @param modifier Modifier to be applied to the divider
 * @param height Optional height for the divider. If not specified, the divider will fill the available height
 */
@Composable
fun DefaultColumnDivider(
    modifier: Modifier = Modifier,
    height: Dp = Dp.Unspecified,
) {

    Box(
        modifier = modifier
            .width(2.dp)
            .then(if (height != Dp.Unspecified) Modifier.height(height) else Modifier)
            .padding(vertical = 8.dp)
            .background(color = Color.White)
    )

    Spacer(
        modifier = Modifier
            .width(10.dp)
    )
}


/**
 * Default implementation of a horizontal divider between rows in the LazyDataTable.
 * 
 * This composable renders a horizontal line that serves as a visual separator between rows
 * in the LazyDataTable. It's used by default in the itemDivider parameter of LazyDataTable
 * if no custom divider is provided.
 *
 * @param modifier Modifier to be applied to the divider, typically controlling width and padding
 */
@Composable
fun DefaultItemDivider(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .background(color = Color.White)
    )
}

@Composable
private fun ColumnDividerContainer(
    columnIndex: Int = 0,
    onWidthChange: (Int, Float) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    columnDivider: @Composable () -> Unit? = {}
) {
    var isDragging by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .then(
                Modifier
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            onWidthChange(columnIndex, delta)
                        },
                        onDragStarted = { isDragging = true },
                        onDragStopped = { isDragging = false }
                    )
                    .pointerHoverIcon(icon = PointerIcon.Crosshair)
                    .alpha(if (isDragging) 0.5f else 1f)
            )
    ) {
        columnDivider()
    }
}

@Composable
private fun ItemDividerContainer(
    rowWidth: Dp,
    modifier: Modifier = Modifier,
    itemDivider: @Composable () -> Unit? = {}
) {
    Box(
        modifier = modifier
            .then(
                Modifier
                    .width(rowWidth)
            )
    ) {
        itemDivider()
    }
}

@Composable
private fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }
