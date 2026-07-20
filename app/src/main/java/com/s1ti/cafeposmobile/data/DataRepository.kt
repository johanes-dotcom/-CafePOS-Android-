package com.s1ti.cafeposmobile.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object DataRepository {
    val products: SnapshotStateList<Product> = mutableStateListOf()
    val transactions: SnapshotStateList<TransactionHistory> = mutableStateListOf()

    fun addProduct(product: Product) {
        products.add(product)
    }

    fun updateProduct(product: Product) {
        val index = products.indexOfFirst { it.id == product.id }
        if (index != -1) {
            products[index] = product
        }
    }

    fun deleteProduct(productId: String) {
        products.removeIf { it.id == productId }
    }

    fun addTransaction(transaction: TransactionHistory) {
        transactions.add(transaction)
    }

    // Statistics calculations with support for filtered transactions
    fun getTotalRevenue(txList: List<TransactionHistory> = transactions): Double = 
        txList.sumOf { it.total }
    
    fun getTotalTransactions(txList: List<TransactionHistory> = transactions): Int = 
        txList.size
    
    fun getAverageOrder(txList: List<TransactionHistory> = transactions): Double {
        val total = getTotalRevenue(txList)
        val count = getTotalTransactions(txList)
        return if (count > 0) total / count else 0.0
    }

    fun getTotalProfit(txList: List<TransactionHistory> = transactions): Double {
        // Profit = Total Revenue - Total Cost Price (COGS)
        return txList.sumOf { tx -> tx.total - tx.totalCostPrice }
    }

    fun getTopProducts(txList: List<TransactionHistory> = transactions): List<TopProduct> {
        val productSales = mutableMapOf<String, Int>()
        val productRevenue = mutableMapOf<String, Double>()
        
        txList.forEach { tx ->
            tx.items.forEach { item ->
                val name = item.product.name
                productSales[name] = productSales.getOrDefault(name, 0) + item.quantity
                productRevenue[name] = productRevenue.getOrDefault(name, 0.0) + (item.product.price * item.quantity)
            }
        }
        
        return productSales.map { (name, qty) ->
            TopProduct(name, qty, productRevenue[name] ?: 0.0)
        }.sortedByDescending { it.quantitySold }.take(10)
    }

    fun getPaymentMethodReport(txList: List<TransactionHistory> = transactions): List<PaymentMethodReport> {
        val methodCounts = mutableMapOf<String, Int>()
        val methodAmounts = mutableMapOf<String, Double>()
        val totalAmount = getTotalRevenue(txList)
        
        txList.forEach { tx ->
            methodCounts[tx.method] = methodCounts.getOrDefault(tx.method, 0) + 1
            methodAmounts[tx.method] = methodAmounts.getOrDefault(tx.method, 0.0) + tx.total
        }
        
        return methodCounts.map { (method, count) ->
            val amount = methodAmounts[method] ?: 0.0
            PaymentMethodReport(
                method = method,
                transactionCount = count,
                totalAmount = amount,
                percentage = if (totalAmount > 0) (amount / totalAmount * 100).toFloat() else 0f
            )
        }.sortedByDescending { it.totalAmount }
    }
}
