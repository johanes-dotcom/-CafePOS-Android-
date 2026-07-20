package com.s1ti.cafeposmobile.data.local.dao

import androidx.room.*
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert
    suspend fun insertTransactionItems(items: List<TransactionItemEntity>)

    @Transaction
    suspend fun insertTransactionWithItems(transaction: TransactionEntity, items: List<TransactionItemEntity>) {
        insertTransaction(transaction)
        insertTransactionItems(items)
    }

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transaction_items WHERE transactionId = :transactionId")
    suspend fun getItemsForTransaction(transactionId: String): List<TransactionItemEntity>

    @Query("SELECT * FROM transaction_items WHERE transactionId IN (:transactionIds)")
    suspend fun getItemsForTransactions(transactionIds: List<String>): List<TransactionItemEntity>

    @Update
    suspend fun updateTransactionStatus(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: String)

    @Query("DELETE FROM transaction_items WHERE transactionId = :transactionId")
    suspend fun deleteTransactionItems(transactionId: String)

    @Transaction
    suspend fun deleteTransactionWithItems(transactionId: String) {
        deleteTransactionItems(transactionId)
        deleteTransaction(transactionId)
    }

    @Query("SELECT * FROM transactions WHERE status IN ('Aktif', 'Diproses', 'Siap') ORDER BY timestamp ASC")
    fun getActiveOrders(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?
}
