package com.mopanesystems.ziposmobile.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.*
import com.mopanesystems.ziposmobile.data.database.POSDatabase
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OfflineQueueManager(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)
    private val database = POSDatabase.getDatabase(context)
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var isOnline = false
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isOnline = true
            processOfflineQueue()
        }

        override fun onLost(network: Network) {
            isOnline = false
        }
    }

    init {
        registerNetworkCallback()
        checkInitialConnectivity()
    }

    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun checkInitialConnectivity() {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        isOnline = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        
        if (isOnline) {
            processOfflineQueue()
        }
    }

    fun queueTransaction(transaction: Transaction) {
        if (isOnline) {
            // Process immediately if online
            processTransaction(transaction)
        } else {
            // Queue for later processing
            queueOfflineTransaction(transaction)
        }
    }

    fun queueInventoryAdjustment(adjustment: InventoryAdjustment) {
        if (isOnline) {
            processInventoryAdjustment(adjustment)
        } else {
            queueOfflineInventoryAdjustment(adjustment)
        }
    }

    fun queueProductUpdate(product: Product) {
        if (isOnline) {
            processProductUpdate(product)
        } else {
            queueOfflineProductUpdate(product)
        }
    }

    fun queueCustomerUpdate(customer: Customer) {
        if (isOnline) {
            processCustomerUpdate(customer)
        } else {
            queueOfflineCustomerUpdate(customer)
        }
    }

    private fun processOfflineQueue() {
        coroutineScope.launch {
            try {
                // Process queued transactions
                val queuedTransactions = database.transactionDao().getQueuedTransactions()
                queuedTransactions.collect { transactions ->
                    transactions.forEach { transaction ->
                        processTransaction(transaction)
                        markTransactionAsProcessed(transaction.id)
                    }
                }

                // Process queued inventory adjustments
                val queuedAdjustments = database.inventoryAdjustmentDao().getQueuedAdjustments()
                queuedAdjustments.collect { adjustments ->
                    adjustments.forEach { adjustment ->
                        processInventoryAdjustment(adjustment)
                        markAdjustmentAsProcessed(adjustment.id)
                    }
                }

                // Process other queued operations...
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    private suspend fun processTransaction(transaction: Transaction) {
        // TODO: Send transaction to server
        // This would typically involve API calls to sync with backend
    }

    private suspend fun processInventoryAdjustment(adjustment: InventoryAdjustment) {
        // TODO: Send inventory adjustment to server
    }

    private suspend fun processProductUpdate(product: Product) {
        // TODO: Send product update to server
    }

    private suspend fun processCustomerUpdate(customer: Customer) {
        // TODO: Send customer update to server
    }

    private suspend fun queueOfflineTransaction(transaction: Transaction) {
        val queuedTransaction = transaction.copy(
            // Mark as queued for offline processing
            status = TransactionStatus.PENDING
        )
        database.transactionDao().insertTransaction(queuedTransaction)
        
        // Schedule sync work
        scheduleSyncWork()
    }

    private suspend fun queueOfflineInventoryAdjustment(adjustment: InventoryAdjustment) {
        database.inventoryAdjustmentDao().insertAdjustment(adjustment)
        scheduleSyncWork()
    }

    private suspend fun queueOfflineProductUpdate(product: Product) {
        database.productDao().updateProduct(product)
        scheduleSyncWork()
    }

    private suspend fun queueOfflineCustomerUpdate(customer: Customer) {
        database.customerDao().updateCustomer(customer)
        scheduleSyncWork()
    }

    private fun scheduleSyncWork() {
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniqueWork(
            "sync_offline_data",
            ExistingWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }

    private suspend fun markTransactionAsProcessed(transactionId: String) {
        // TODO: Mark transaction as processed in local database
    }

    private suspend fun markAdjustmentAsProcessed(adjustmentId: String) {
        // TODO: Mark adjustment as processed in local database
    }

    fun isOnline(): Boolean = isOnline

    fun cleanup() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val offlineQueueManager = OfflineQueueManager(applicationContext)
            offlineQueueManager.processOfflineQueue()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
