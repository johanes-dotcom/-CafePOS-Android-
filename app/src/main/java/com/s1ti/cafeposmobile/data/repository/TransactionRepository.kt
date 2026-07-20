package com.s1ti.cafeposmobile.data.repository

import com.s1ti.cafeposmobile.data.local.dao.TransactionDao
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val activeOrders: Flow<List<TransactionEntity>> = transactionDao.getActiveOrders()

    suspend fun insertTransaction(transaction: TransactionEntity, items: List<TransactionItemEntity>) {
        transactionDao.insertTransactionWithItems(transaction, items)
    }

    suspend fun updateTransactionStatus(transaction: TransactionEntity) {
        transactionDao.updateTransactionStatus(transaction)
    }

    suspend fun deleteTransaction(transactionId: String) {
        transactionDao.deleteTransactionWithItems(transactionId)
    }

    suspend fun getItemsForTransaction(transactionId: String): List<TransactionItemEntity> {
        return transactionDao.getItemsForTransaction(transactionId)
    }

    suspend fun getItemsForTransactions(transactionIds: List<String>): List<TransactionItemEntity> {
        return transactionDao.getItemsForTransactions(transactionIds)
    }

    suspend fun getTransactionById(id: String): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }
}
