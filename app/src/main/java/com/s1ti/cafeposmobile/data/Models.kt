package com.s1ti.cafeposmobile.data

import android.net.Uri

data class Product(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val price: Double,
    val category: String,
    val description: String = "",
    val imageUri: Uri? = null,
    val isAvailable: Boolean = true,
    val costPrice: Double = 0.0 // Untuk perhitungan profit
)

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class Order(
    val id: String,
    val table: String,
    val items: String,
    var status: String,
    val note: String = "",
    val customerName: String = "Pelanggan Umum",
    val totalAmount: Double = 0.0
)

data class TransactionHistory(
    val id: String,
    val date: String,
    val time: String,
    val total: Double,
    val method: String,
    val status: String,
    val cashierName: String = "Budi Santoso",
    val customerName: String = "",
    val items: List<CartItem> = emptyList(),
    val discount: Double = 0.0,
    val tax: Double = 0.0,
    val totalCostPrice: Double = 0.0, // Untuk perhitungan profit
    val amountPaid: Double = 0.0
)

data class SalesSummary(
    val totalRevenue: Double,
    val totalTransactions: Int,
    val totalProductsSold: Int,
    val totalCustomers: Int,
    val averageTransactionValue: Double,
    val totalDiscount: Double,
    val totalProfit: Double
)

data class PaymentMethodReport(
    val method: String,
    val transactionCount: Int,
    val totalAmount: Double,
    val percentage: Float
)

data class TopProduct(
    val name: String,
    val quantitySold: Int,
    val totalRevenue: Double
)

data class CashierPerformance(
    val name: String,
    val transactionCount: Int,
    val totalRevenue: Double,
    val averageValue: Double
)
