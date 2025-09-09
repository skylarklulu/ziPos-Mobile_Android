package com.mopanesystems.ziposmobile.data.repository

import com.mopanesystems.ziposmobile.data.database.dao.StoreDao
import com.mopanesystems.ziposmobile.data.database.dao.UserDao
import com.mopanesystems.ziposmobile.data.database.dao.UserPermissionDao
import com.mopanesystems.ziposmobile.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    private val storeDao: StoreDao,
    private val userDao: UserDao,
    private val userPermissionDao: UserPermissionDao
) {
    // Store operations
    fun getAllActiveStores(): Flow<List<Store>> = storeDao.getAllActiveStores()
    
    suspend fun getStoreById(id: String): Store? = storeDao.getStoreById(id)
    
    suspend fun insertStore(store: Store) = storeDao.insertStore(store)
    
    suspend fun updateStore(store: Store) = storeDao.updateStore(store)
    
    suspend fun deleteStore(store: Store) = storeDao.deleteStore(store)
    
    suspend fun deactivateStore(storeId: String) = storeDao.deactivateStore(storeId)
    
    // User operations
    fun getAllActiveUsers(): Flow<List<User>> = userDao.getAllActiveUsers()
    
    fun getUsersByStore(storeId: String): Flow<List<User>> = userDao.getUsersByStore(storeId)
    
    suspend fun getUserById(id: String): User? = userDao.getUserById(id)
    
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    fun getUsersByRole(role: UserRole): Flow<List<User>> = userDao.getUsersByRole(role)
    
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun deactivateUser(userId: String) = userDao.deactivateUser(userId)
    
    suspend fun updateLastLogin(userId: String, loginTime: LocalDateTime) = userDao.updateLastLogin(userId, loginTime)
    
    // User Permission operations
    fun getUserPermissions(userId: String): Flow<List<UserPermission>> = userPermissionDao.getUserPermissions(userId)
    
    suspend fun getUserPermission(userId: String, permission: String): UserPermission? = 
        userPermissionDao.getUserPermission(userId, permission)
    
    suspend fun insertPermission(permission: UserPermission) = userPermissionDao.insertPermission(permission)
    
    suspend fun updatePermission(permission: UserPermission) = userPermissionDao.updatePermission(permission)
    
    suspend fun deletePermission(permission: UserPermission) = userPermissionDao.deletePermission(permission)
    
    suspend fun deleteUserPermissions(userId: String) = userPermissionDao.deleteUserPermissions(userId)
}
