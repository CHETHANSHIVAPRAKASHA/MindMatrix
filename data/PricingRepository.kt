package com.example.santepriceindex.data

import kotlinx.coroutines.delay

interface PricingRepository {
    suspend fun getTodayPrices(): List<Commodity>
}

class MockFirebasePricingRepository : PricingRepository {
    override suspend fun getTodayPrices(): List<Commodity> {
        delay(1000) // Simulate network delay
        return listOf(
            Commodity("1", "Onion (Kg)", 30.0, Trend.UP),
            Commodity("2", "Tomato (Kg)", 20.0, Trend.DOWN),
            Commodity("3", "Potato (Kg)", 25.0, Trend.STABLE),
            Commodity("4", "Cabbage (Kg)", 15.0, Trend.STABLE),
            Commodity("5", "Carrot (Kg)", 40.0, Trend.UP)
        )
    }
}
