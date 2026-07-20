package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.ui.components.*
import com.s1ti.cafeposmobile.ui.theme.*
import java.text.NumberFormat
import java.util.*

@Composable
fun CashierDashboardScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(LocalContext.current))
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    val stats by viewModel.stats.collectAsState()

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "dashboard", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!isExpanded && !isMedium) {
                    CashierBottomNavigation(currentRoute = "dashboard", onNavigate = onNavigate)
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = if (isExpanded) 32.dp else 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.dashboard),
                        style = if (isExpanded) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Selamat bekerja!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    if (isExpanded) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            CafeStatCard(title = "Total Penjualan", value = formatCurrencySimple(stats.totalRevenue), trend = "Real-time", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(12.dp))
                            CafeStatCard(title = "Total Transaksi", value = stats.totalTransactions.toString(), trend = "Real-time", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(12.dp))
                            CafeStatCard(title = "Total Profit", value = formatCurrencySimple(stats.totalProfit), trend = "Real-time", modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(12.dp))
                            CafeStatCard(title = "Average Order", value = formatCurrencySimple(stats.averageOrder), trend = "Real-time", modifier = Modifier.weight(1f))
                        }
                    } else {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CafeStatCard(title = "Total Penjualan", value = formatCurrencySimple(stats.totalRevenue), trend = "Real-time", modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(12.dp))
                                CafeStatCard(title = "Total Transaksi", value = stats.totalTransactions.toString(), trend = "Real-time", modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CafeStatCard(title = "Total Profit", value = formatCurrencySimple(stats.totalProfit), trend = "Real-time", modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(12.dp))
                                CafeStatCard(title = "Average Order", value = formatCurrencySimple(stats.averageOrder), trend = "Real-time", modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Penjualan Hari Ini",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SimpleLineChart(height = if (isExpanded) 250 else 150)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Menu Cepat",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val menuIcons = listOf(
                        Triple(Icons.Default.RestaurantMenu, stringResource(R.string.transaction), "transaction"),
                        Triple(Icons.Default.ShoppingBag, stringResource(R.string.orders), "orders"),
                        Triple(Icons.Default.ReceiptLong, stringResource(R.string.history), "history"),
                        Triple(Icons.Default.Person, stringResource(R.string.profile), "profile"),
                        Triple(Icons.Default.BarChart, stringResource(R.string.report), "report"),
                        Triple(Icons.Default.Inventory, stringResource(R.string.product_menu), "product_menu"),
                        Triple(Icons.Default.Settings, stringResource(R.string.settings), "settings")
                    )

                    val columns = if (isExpanded) 6 else if (isMedium) 4 else 3
                    
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        menuIcons.chunked(columns).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isExpanded) Arrangement.spacedBy(24.dp) else Arrangement.SpaceBetween
                            ) {
                                rowItems.forEach { (icon, label, route) ->
                                    CafeIconButton(
                                        icon = icon, 
                                        label = label, 
                                        onClick = { onNavigate(route) },
                                        modifier = if (isExpanded) Modifier.width(100.dp) else Modifier.weight(1f)
                                    )
                                }
                                // Fill remaining space if row is not full
                                if (!isExpanded && rowItems.size < columns) {
                                    repeat(columns - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                } else if (isExpanded && rowItems.size < columns) {
                                     repeat(columns - rowItems.size) {
                                        Spacer(modifier = Modifier.width(100.dp))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    CafeButton(
                        text = stringResource(R.string.new_transaction),
                        onClick = { onNavigate("transaction") },
                        modifier = if (isExpanded) Modifier.width(300.dp) else Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

fun formatCurrencySimple(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
}
