package com.mopanesystems.ziposmobile.ui.offline

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mopanesystems.ziposmobile.services.OfflineQueueManager
import com.mopanesystems.ziposmobile.services.SyncService
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class OfflineStatusViewModel : ViewModel() {

    private lateinit var offlineQueueManager: OfflineQueueManager
    private lateinit var syncService: SyncService

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline: LiveData<Boolean> = _isOnline

    private val _queuedItemsCount = MutableLiveData<Int>()
    val queuedItemsCount: LiveData<Int> = _queuedItemsCount

    private val _lastSyncTime = MutableLiveData<String>()
    val lastSyncTime: LiveData<String> = _lastSyncTime

    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> = _syncStatus

    fun initialize(context: Context) {
        offlineQueueManager = OfflineQueueManager(context)
        syncService = SyncService(context)
        
        // Start monitoring connection status
        startMonitoring()
    }

    private fun startMonitoring() {
        viewModelScope.launch {
            // Monitor connection status
            _isOnline.value = offlineQueueManager.isOnline()
            
            // Get queued items count
            updateQueuedItemsCount()
            
            // Get last sync time
            updateLastSyncTime()
            
            // Get sync status
            updateSyncStatus()
        }
    }

    private suspend fun updateQueuedItemsCount() {
        // TODO: Get actual count from database
        _queuedItemsCount.value = 0
    }

    private suspend fun updateLastSyncTime() {
        val lastSync = syncService.getLastSyncTime()
        _lastSyncTime.value = lastSync?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "Never"
    }

    private suspend fun updateSyncStatus() {
        val status = syncService.getSyncStatus()
        _syncStatus.value = status?.name ?: "Unknown"
    }

    fun syncNow() {
        viewModelScope.launch {
            _syncStatus.value = "Syncing..."
            syncService.syncAllData()
            updateLastSyncTime()
            updateSyncStatus()
        }
    }

    fun refreshStatus() {
        startMonitoring()
    }
}
