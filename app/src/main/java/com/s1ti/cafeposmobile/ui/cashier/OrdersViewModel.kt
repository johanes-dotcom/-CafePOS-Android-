package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: TransactionRepository) : ViewModel() {

    val activeOrders: StateFlow<List<TransactionEntity>> = repository.activeOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateStatus(transaction: TransactionEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateTransactionStatus(transaction.copy(status = newStatus))
        }
    }

    fun deleteOrder(transactionId: String) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionId)
        }
    }

    suspend fun getItemsForTransaction(transactionId: String): List<TransactionItemEntity> {
        return repository.getItemsForTransaction(transactionId)
    }
}
