package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.CartItem
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun completeTransaction(
        cartItems: List<CartItem>,
        totalAmount: Double,
        totalCostPrice: Double,
        paymentMethod: String,
        amountPaid: Double,
        customerName: String = "",
        tableNumber: String = "",
        note: String = "",
        cashierName: String = "Kasir",
        onComplete: (TransactionHistory) -> Unit
    ) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val dateStr = dateFormat.format(Date(timestamp))
            val transactionId = "INV-$dateStr-${System.currentTimeMillis().toString().takeLast(4)}"
            
            // Perbaikan: Transaksi baru dimulai dengan status "Aktif" 
            // agar muncul di halaman Pesanan sesuai permintaan user.
            val transaction = TransactionEntity(
                id = transactionId,
                timestamp = timestamp,
                totalAmount = totalAmount,
                totalCostPrice = totalCostPrice,
                paymentMethod = paymentMethod,
                status = "Aktif", 
                customerName = customerName,
                tableNumber = tableNumber,
                note = note,
                cashierName = cashierName,
                amountPaid = amountPaid,
                tax = 0.0
            )

            val items = cartItems.map { item ->
                TransactionItemEntity(
                    transactionId = transactionId,
                    productId = item.product.id,
                    productName = item.product.name,
                    price = item.product.price,
                    quantity = item.quantity,
                    costPrice = item.product.costPrice
                )
            }

            repository.insertTransaction(transaction, items)
            
            val now = Date(timestamp)
            val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            
            val history = TransactionHistory(
                id = transactionId,
                date = displayDateFormat.format(now),
                time = timeFormat.format(now),
                total = totalAmount,
                method = paymentMethod,
                status = "Aktif",
                items = cartItems.toList(),
                totalCostPrice = totalCostPrice,
                amountPaid = amountPaid,
                cashierName = cashierName
            )
            
            onComplete(history)
        }
    }
}
