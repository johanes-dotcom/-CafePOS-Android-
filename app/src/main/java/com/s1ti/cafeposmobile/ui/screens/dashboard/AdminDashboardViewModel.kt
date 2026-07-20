package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class AdminDashboardViewModel(private val repository: TransactionRepository) : ViewModel() {

    val summary: StateFlow<DashboardSummary> = repository.allTransactions
        .map { transactions ->
            val successfulTransactions = transactions.filter { it.status == "Selesai" }
            
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfToday = calendar.timeInMillis
            
            val todayTransactions = successfulTransactions.filter { it.timestamp >= startOfToday }
            
            val totalPenjualanHariIni = todayTransactions.sumOf { it.totalAmount }.toLong()
            val totalTransaksi = successfulTransactions.size
            val totalProfit = successfulTransactions.sumOf { it.totalAmount - it.totalCostPrice }.toLong()
            val averageOrder = if (totalTransaksi > 0) successfulTransactions.sumOf { it.totalAmount }.toLong() / totalTransaksi else 0L

            // Simple chart data: hourly sales for today (8 slots as in current UI)
            val hourlySales = MutableList(8) { 0f }
            todayTransactions.forEach { tx ->
                val hour = Calendar.getInstance().apply { timeInMillis = tx.timestamp }.get(Calendar.HOUR_OF_DAY)
                val index = (hour / 3).coerceIn(0, 7) // Group into 8 slots of 3 hours
                hourlySales[index] += tx.totalAmount.toFloat()
            }

            DashboardSummary(
                totalPenjualanHariIni = totalPenjualanHariIni,
                totalPenjualanDeltaPercent = 0.0, // Comparison logic can be added later
                totalTransaksi = totalTransaksi,
                totalTransaksiDeltaPercent = 0.0,
                totalProfit = totalProfit,
                totalProfitDeltaPercent = 0.0,
                averageOrder = averageOrder,
                averageOrderDeltaPercent = 0.0,
                grafikPenjualanHarian = hourlySales
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardSummary(
                totalPenjualanHariIni = 0,
                totalTransaksi = 0,
                totalProfit = 0,
                averageOrder = 0,
                grafikPenjualanHarian = List(8) { 0f }
            )
        )
}
