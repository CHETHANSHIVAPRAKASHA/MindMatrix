package com.example.santepriceindex.data

data class Commodity(
    val id: String,
    val name: String,
    val mandiPrice: Double,
    val trend: Trend // UP, DOWN, STABLE
)

enum class Trend {
    UP, DOWN, STABLE
}
