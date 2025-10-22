package dataTable.samples.finance.barchart


/*
 * Designed and developed by 2024 androidpoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import androidx.compose.ui.graphics.Color

/**
 * Data class representing data for a simple bar chart with single bars.
 *
 * @property labelsList List of labels for the X-axis
 * @property values List of values for each bar
 * @property colors List of colors for each bar
 */
data class SimpleBarChartData(
    val labelsList: List<String>,
    val values: List<Float>,
    val colors: List<Color>,
)

