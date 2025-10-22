package dataTable.samples.finance

import kotlinx.serialization.Serializable

@Serializable
data class Portfolio(
    val items: List<PortfolioItem>
)

@Serializable
data class PortfolioItem(
    val ticker: String,
    val name: String,
    val price: Float,
    val instrument:String,
    val quantity: Int,
    val purchaseDate: String,
    val purchasePrice: Int,
    val timeline: List<Double>
)
