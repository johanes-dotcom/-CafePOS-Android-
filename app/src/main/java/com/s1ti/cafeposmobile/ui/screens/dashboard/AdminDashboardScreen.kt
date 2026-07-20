package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.DashboardSummaryCard
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.EmptyStatePlaceholder
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.QuickMenuGrid
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.QuickMenuItem
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.SimpleLineChart
import com.s1ti.cafeposmobile.ui.screens.menu.MenuManagementScreen
import com.s1ti.cafeposmobile.ui.screens.stok.StokManagementScreen
import com.s1ti.cafeposmobile.util.CurrencyFormatter

private data class AdminTab(val label: String, val icon: ImageVector)

private val adminTabs = listOf(
    AdminTab("Beranda", Icons.Default.Home),
    AdminTab("Laporan", Icons.Default.Assessment),
    AdminTab("Menu", Icons.Default.RestaurantMenu),
    AdminTab("Stok", Icons.Default.Inventory2),
    AdminTab("Lainnya", Icons.Default.MoreHoriz)
)

@Composable
fun AdminDashboardScreen(
    ownerName: String = "Owner",
    onLogout: () -> Unit,
    dashboardViewModel: AdminDashboardViewModel = viewModel(factory = AdminDashboardViewModelFactory(LocalContext.current)),
    reportViewModel: AdminReportViewModel = viewModel(factory = AdminDashboardViewModelFactory(LocalContext.current))
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAnalytics by remember { mutableStateOf(false) }
    val summary by dashboardViewModel.summary.collectAsState()

    if (showAnalytics) {
        AnalyticsScreen(onBack = { showAnalytics = false })
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                adminTabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> BerandaTab(
                ownerName = ownerName,
                summary = summary,
                onQuickMenuClick = { targetTab -> selectedTab = targetTab },
                onSeeAnalytics = { showAnalytics = true },
                modifier = Modifier.padding(padding)
            )
            1 -> LaporanPenjualanScreen(
                viewModel = reportViewModel,
                modifier = Modifier.padding(padding)
            )
            2 -> MenuManagementScreen(modifier = Modifier.padding(padding))
            3 -> StokManagementScreen(modifier = Modifier.padding(padding))
            4 -> LainnyaTab(
                ownerName = ownerName,
                onLogout = onLogout,
                modifier = Modifier.padding(padding)
            )
            else -> EmptyStatePlaceholder(
                text = "Fitur \"${adminTabs[selectedTab].label}\" menyusul pada increment berikutnya sesuai SRS.",
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun BerandaTab(
    ownerName: String,
    summary: DashboardSummary,
    onQuickMenuClick: (Int) -> Unit,
    onSeeAnalytics: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Good Morning", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = ownerName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSummaryCard(
                    title = "Total Penjualan",
                    value = CurrencyFormatter.formatRupiah(summary.totalPenjualanHariIni),
                    deltaPercent = summary.totalPenjualanDeltaPercent,
                    modifier = Modifier.weight(1f)
                )
                DashboardSummaryCard(
                    title = "Total Transaksi",
                    value = summary.totalTransaksi.toString(),
                    deltaPercent = summary.totalTransaksiDeltaPercent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSummaryCard(
                    title = "Total Profit",
                    value = CurrencyFormatter.formatRupiah(summary.totalProfit),
                    deltaPercent = summary.totalProfitDeltaPercent,
                    modifier = Modifier.weight(1f)
                )
                DashboardSummaryCard(
                    title = "Average Order",
                    value = CurrencyFormatter.formatRupiah(summary.averageOrder),
                    deltaPercent = summary.averageOrderDeltaPercent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Penjualan Hari Ini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = onSeeAnalytics) {
                    Text("Lihat Analitik")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SimpleLineChart(dataPoints = summary.grafikPenjualanHarian)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Menu Cepat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            QuickMenuGrid(
                items = listOf(
                    QuickMenuItem("Menu", Icons.Default.RestaurantMenu) { onQuickMenuClick(2) },
                    QuickMenuItem("Stok", Icons.Default.Inventory2) { onQuickMenuClick(3) },
                    QuickMenuItem("Karyawan", Icons.Default.People) { onQuickMenuClick(4) },
                    QuickMenuItem("Laporan", Icons.Default.Assessment) { onQuickMenuClick(1) }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LainnyaTab(
    ownerName: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(ownerName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Owner", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onLogout) {
            Text("Logout")
        }
    }
}
