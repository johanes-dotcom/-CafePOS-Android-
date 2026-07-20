package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AdminReportViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _selectedPeriode = MutableStateFlow(PeriodeLaporan.HARIAN)
    val selectedPeriode: StateFlow<PeriodeLaporan> = _selectedPeriode

    private val _transactionItems = MutableStateFlow<List<TransactionItemEntity>>(emptyList())
    val transactionItems: StateFlow<List<TransactionItemEntity>> = _transactionItems

    val reportData: StateFlow<LaporanPenjualanData?> = combine(
        repository.allTransactions,
        _selectedPeriode
    ) { transactions, periode ->
        val filteredTransactions = filterByPeriode(transactions, periode)
        
        if (filteredTransactions.isEmpty()) {
            LaporanPenjualanData(
                periode = periode,
                tanggalLabel = getPeriodeLabel(periode),
                totalPenjualan = 0,
                deltaPercent = 0.0,
                totalTransaksi = 0,
                totalDiskon = 0,
                totalPajak = 0,
                totalService = 0,
                totalPembayaran = 0,
                totalProfitEstimasi = 0,
                metodePembayaran = emptyList(),
                menuTerlaris = emptyList(),
                daftarTransaksi = emptyList()
            )
        } else {
            val totalPenjualan = filteredTransactions.sumOf { it.totalAmount }.toLong()
            val totalTransaksi = filteredTransactions.size
            val totalProfit = filteredTransactions.sumOf { it.totalAmount - it.totalCostPrice }.toLong()
            
            // Metode Pembayaran
            val methods = filteredTransactions.groupBy { it.paymentMethod }
                .map { entry ->
                    val method = entry.key
                    val txs = entry.value
                    val nominal = txs.sumOf { it.totalAmount }.toLong()
                    MetodePembayaranRingkasan(
                        metode = method,
                        nominal = nominal,
                        persentase = if (totalPenjualan > 0) (nominal * 100 / totalPenjualan).toInt() else 0
                    )
                }

            // Menu Terlaris (Needs items)
            val transactionIds = filteredTransactions.map { it.id }
            val allItems = repository.getItemsForTransactions(transactionIds)
            
            val topMenus = allItems.groupBy { it.productName }
                .map { entry ->
                    MenuTerlaris(entry.key, entry.value.sumOf { it.quantity })
                }
                .sortedByDescending { it.jumlahTerjual }
                .take(5)

            LaporanPenjualanData(
                periode = periode,
                tanggalLabel = getPeriodeLabel(periode),
                totalPenjualan = totalPenjualan,
                deltaPercent = 0.0,
                totalTransaksi = totalTransaksi,
                totalDiskon = 0,
                totalPajak = (totalPenjualan * 0.1).toLong(), 
                totalService = 0,
                totalPembayaran = totalPenjualan,
                totalProfitEstimasi = totalProfit,
                metodePembayaran = methods,
                menuTerlaris = topMenus,
                daftarTransaksi = filteredTransactions
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun setPeriode(periode: PeriodeLaporan) {
        _selectedPeriode.value = periode
    }

    fun loadTransactionItems(transactionId: String) {
        viewModelScope.launch {
            _transactionItems.value = repository.getItemsForTransaction(transactionId)
        }
    }

    private fun filterByPeriode(transactions: List<TransactionEntity>, periode: PeriodeLaporan): List<TransactionEntity> {
        val calendar = Calendar.getInstance()
        
        return when (periode) {
            PeriodeLaporan.HARIAN -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                transactions.filter { it.timestamp >= calendar.timeInMillis && it.status == "Selesai" }
            }
            PeriodeLaporan.KEMARIN -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val start = calendar.timeInMillis
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val end = calendar.timeInMillis
                transactions.filter { it.timestamp in start..end && it.status == "Selesai" }
            }
            PeriodeLaporan.MINGGUAN -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                transactions.filter { it.timestamp >= calendar.timeInMillis && it.status == "Selesai" }
            }
            PeriodeLaporan.BULANAN -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                transactions.filter { it.timestamp >= calendar.timeInMillis && it.status == "Selesai" }
            }
            PeriodeLaporan.TAHUNAN -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                transactions.filter { it.timestamp >= calendar.timeInMillis && it.status == "Selesai" }
            }
        }
    }

    private fun getPeriodeLabel(periode: PeriodeLaporan): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault())
        val calendar = Calendar.getInstance()
        return when (periode) {
            PeriodeLaporan.HARIAN -> sdf.format(calendar.time)
            PeriodeLaporan.KEMARIN -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                sdf.format(calendar.time)
            }
            PeriodeLaporan.MINGGUAN -> "Minggu Ini"
            PeriodeLaporan.BULANAN -> java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault()).format(calendar.time)
            PeriodeLaporan.TAHUNAN -> calendar.get(Calendar.YEAR).toString()
        }
    }
}
