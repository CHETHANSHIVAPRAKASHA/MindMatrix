package com.example.santepriceindex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.santepriceindex.data.Commodity
import com.example.santepriceindex.data.Trend

@Composable
fun PriceWatchScreen(viewModel: MainViewModel) {
    val commodities by viewModel.commodities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "Today's Mandi Prices", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(commodities) { item ->
                    CommodityRow(item)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun CommodityRow(item: Commodity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "₹${item.mandiPrice}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = when(item.trend) {
                    Trend.UP -> Icons.Filled.ArrowUpward
                    Trend.DOWN -> Icons.Filled.ArrowDownward
                    Trend.STABLE -> Icons.Filled.Remove
                },
                contentDescription = null,
                tint = when(item.trend) {
                    Trend.UP -> Color.Red
                    Trend.DOWN -> Color.Green
                    Trend.STABLE -> Color.Gray
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfitCalcScreen(viewModel: MainViewModel) {
    val commodities by viewModel.commodities.collectAsState()
    val selectedCommodity by viewModel.selectedCommodity.collectAsState()
    val transportCost by viewModel.transportCost.collectAsState()
    val wastePercentage by viewModel.wastePercentage.collectAsState()
    val desiredProfit by viewModel.desiredProfit.collectAsState()
    val calculatedRRP by viewModel.calculatedRRP.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Profit Calculator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for Commodity
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCommodity?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Commodity") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                commodities.forEach { commodity ->
                    DropdownMenuItem(
                        text = { Text(commodity.name) },
                        onClick = {
                            viewModel.selectCommodity(commodity)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Base Price: ₹${selectedCommodity?.mandiPrice ?: 0.0}")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = if (transportCost == 0.0) "" else transportCost.toString(),
            onValueChange = { viewModel.updateTransportCost(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Transport Cost (₹/kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (wastePercentage == 0.0) "" else wastePercentage.toString(),
            onValueChange = { viewModel.updateWastePercentage(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Waste Percentage (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (desiredProfit == 0.0) "" else desiredProfit.toString(),
            onValueChange = { viewModel.updateDesiredProfit(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Desired Profit (₹/kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recommended Retail Price (RRP)", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = String.format("₹%.2f", calculatedRRP),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun PriceBoardScreen(viewModel: MainViewModel) {
    val commodities by viewModel.commodities.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "TODAY'S PRICES",
                color = Color.Yellow,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(commodities) { item ->
                    // For demo, displaying a mock calculated price
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name.uppercase(),
                            color = Color.Yellow,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${item.mandiPrice.toInt() + 10}", // Dummy retail price
                            color = Color.Yellow,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.DarkGray))
                }
            }
        }
    }
}

@Composable
fun TrendsScreen(viewModel: MainViewModel) {
    val commodities by viewModel.commodities.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Market Trends (Simulated)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(commodities) { item ->
                TrendRow(item)
                Divider()
            }
        }
    }
}

@Composable
fun TrendRow(item: Commodity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Current: ₹${item.mandiPrice}", style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val (icon, color, text) = when (item.trend) {
                Trend.UP -> Triple(Icons.Filled.TrendingUp, Color.Red, "Rising")
                Trend.DOWN -> Triple(Icons.Filled.TrendingDown, Color(0xFF008000), "Falling") // Dark Green
                Trend.STABLE -> Triple(Icons.Filled.TrendingFlat, Color.Gray, "Stable")
            }
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = color, style = MaterialTheme.typography.titleMedium)
        }
    }
}
