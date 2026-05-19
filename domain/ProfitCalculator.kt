package com.example.santepriceindex.domain

class ProfitCalculator {
    /**
     * Calculates the Recommended Retail Price (RRP)
     * @param basePrice The Mandi price
     * @param transportCost Transport cost per kg
     * @param wastePercentage Expected waste percentage (e.g. 5 for 5%)
     * @param desiredProfitMargin Desired profit per kg
     */
    fun calculateRRP(
        basePrice: Double,
        transportCost: Double,
        wastePercentage: Double,
        desiredProfitMargin: Double
    ): Double {
        // Effective cost per kg after accounting for waste.
        // If 10% is wasted, 1 kg bought only yields 0.9 kg sellable.
        // Cost of 1 sellable kg = (Base + Transport) / (1 - wastePercentage/100)
        
        val baseCostPerKg = basePrice + transportCost
        val usableFraction = 1.0 - (wastePercentage / 100.0)
        
        val effectiveCost = if (usableFraction > 0.0) {
            baseCostPerKg / usableFraction
        } else {
            baseCostPerKg
        }

        return effectiveCost + desiredProfitMargin
    }
}
