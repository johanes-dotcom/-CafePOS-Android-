package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class ReportViewModel(private val repository: TransactionRepository) : ViewModel() {

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getItemsForTransaction(transactionId: String): List<TransactionItemEntity> {
        return repository.getItemsForTransaction(transactionId)
    }

    fun filterTransactionsByPeriod(transactions: List<TransactionEntity>, period: String): List<TransactionEntity> {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        
        return when (period) {
            "Hari Ini" -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startOfDay = calendar.timeInMillis
                transactions.filter { it.timestamp in startOfDay..now }
            }
            "Kemarin" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startOfYesterday = calendar.timeInMillis
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endOfYesterday = calendar.timeInMillis
                transactions.filter { it.timestamp in startOfYesterday..endOfYesterday }
            }
            "Minggu Ini" -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                val startOfWeek = calendar.timeInMillis
                transactions.filter { it.timestamp in startOfWeek..now }
            }
            "Bulan Ini" -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                val startOfMonth = calendar.timeInMillis
                transactions.filter { it.timestamp in startOfMonth..now }
            }
            "Tahun Ini" -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                val startOfYear = calendar.timeInMillis
                transactions.filter { it.timestamp in startOfYear..now }
            }
            else -> transactions
        }
    }
}
