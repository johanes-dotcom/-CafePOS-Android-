package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.CartItem
import com.s1ti.cafeposmobile.data.Product
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(private val repository: TransactionRepository) : ViewModel() {

    val allTransactions: StateFlow<List<TransactionHistory>> = repository.allTransactions
        .map { entities ->
            entities.map { entity ->
                val items = repository.getItemsForTransaction(entity.id).map { item ->
                    CartItem(
                        product = Product(
                            id = item.productId,
                            name = item.productName,
                            price = item.price,
                            category = "",
                            costPrice = item.costPrice
                        ),
                        quantity = item.quantity
                    )
                }
                
                val date = Date(entity.timestamp)
                TransactionHistory(
                    id = entity.id,
                    date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date),
                    time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date),
                    total = entity.totalAmount,
                    method = entity.paymentMethod,
                    status = entity.status,
                    cashierName = entity.cashierName,
                    customerName = entity.customerName,
                    items = items,
                    tax = entity.tax,
                    totalCostPrice = entity.totalCostPrice,
                    amountPaid = entity.amountPaid
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
