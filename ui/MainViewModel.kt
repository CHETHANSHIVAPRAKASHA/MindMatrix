package com.example.santepriceindex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.santepriceindex.data.Commodity
import com.example.santepriceindex.data.MockFirebasePricingRepository
import com.example.santepriceindex.data.PricingRepository
import com.example.santepriceindex.domain.ProfitCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository: PricingRepository = MockFirebasePricingRepository()
    val profitCalculator = ProfitCalculator()

    private val _commodities = MutableStateFlow<List<Commodity>>(emptyList())
    val commodities: StateFlow<List<Commodity>> = _commodities.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Profit Calculator State
    private val _selectedCommodity = MutableStateFlow<Commodity?>(null)
    val selectedCommodity: StateFlow<Commodity?> = _selectedCommodity.asStateFlow()

    private val _transportCost = MutableStateFlow(0.0)
    val transportCost: StateFlow<Double> = _transportCost.asStateFlow()

    private val _wastePercentage = MutableStateFlow(0.0)
    val wastePercentage: StateFlow<Double> = _wastePercentage.asStateFlow()

    private val _desiredProfit = MutableStateFlow(0.0)
    val desiredProfit: StateFlow<Double> = _desiredProfit.asStateFlow()

    private val _calculatedRRP = MutableStateFlow(0.0)
    val calculatedRRP: StateFlow<Double> = _calculatedRRP.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getTodayPrices()
            _commodities.value = data
            if (data.isNotEmpty()) {
                _selectedCommodity.value = data[0]
            }
            _isLoading.value = false
            recalculate()
        }
    }

    fun selectCommodity(commodity: Commodity) {
        _selectedCommodity.value = commodity
        recalculate()
    }

    fun updateTransportCost(cost: Double) {
        _transportCost.value = cost
        recalculate()
    }

    fun updateWastePercentage(percentage: Double) {
        _wastePercentage.value = percentage
        recalculate()
    }

    fun updateDesiredProfit(profit: Double) {
        _desiredProfit.value = profit
        recalculate()
    }

    private fun recalculate() {
        val commodity = _selectedCommodity.value ?: return
        _calculatedRRP.value = profitCalculator.calculateRRP(
            basePrice = commodity.mandiPrice,
            transportCost = _transportCost.value,
            wastePercentage = _wastePercentage.value,
            desiredProfitMargin = _desiredProfit.value
        )
    }
}
