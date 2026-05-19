package com.example.santepriceindex.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object PriceWatch : Screen("watch", "Prices", Icons.Filled.List)
    object ProfitCalc : Screen("calc", "Profit Calc", Icons.Filled.Calculate)
    object PriceBoard : Screen("board", "Board", Icons.Filled.Tv)
    object Trends : Screen("trends", "Trends", Icons.Filled.Analytics)
}

val items = listOf(
    Screen.PriceWatch,
    Screen.ProfitCalc,
    Screen.PriceBoard,
    Screen.Trends
)

@Composable
fun SanteApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.PriceWatch.route, Modifier.padding(innerPadding)) {
            composable(Screen.PriceWatch.route) { PriceWatchScreen(viewModel) }
            composable(Screen.ProfitCalc.route) { ProfitCalcScreen(viewModel) }
            composable(Screen.PriceBoard.route) { PriceBoardScreen(viewModel) }
            composable(Screen.Trends.route) { TrendsScreen(viewModel) }
        }
    }
}
