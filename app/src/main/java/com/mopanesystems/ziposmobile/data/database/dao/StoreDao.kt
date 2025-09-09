package com.mopanesystems.ziposmobile.data.database.dao

import androidx.room.*
import com.mopanesystems.ziposmobile.data.model.Store
import com.mopanesystems.ziposmobile.data.model.User
import com.mopanesystems.ziposmobile.data.model.UserPermission
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE id = :id")
    suspend fun getStoreById(id: String): Store?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: Store)

    @Update
    suspend fun updateStore(store: Store)

    @Delete
    suspend fun deleteStore(store: Store)

    @Query("UPDATE stores SET isActive = 0 WHERE id = :storeId")
    suspend fun deactivateStore(storeId: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY lastName, firstName ASC")
    fun getAllActiveUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE storeId = :storeId AND isActive = 1 ORDER BY lastName, firstName ASC")
    fun getUsersByStore(storeId: String): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND isActive = 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND isActive = 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE role = :role AND isActive = 1 ORDER BY lastName, firstName ASC")
    fun getUsersByRole(role: com.mopanesystems.ziposmobile.data.model.UserRole): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE users SET isActive = 0 WHERE id = :userId")
    suspend fun deactivateUser(userId: String)

    @Query("UPDATE users SET lastLoginAt = :loginTime WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, loginTime: java.time.LocalDateTime)
}

@Dao
interface UserPermissionDao {
    @Query("SELECT * FROM user_permissions WHERE userId = :userId")
    fun getUserPermissions(userId: String): Flow<List<UserPermission>>

    @Query("SELECT * FROM user_permissions WHERE userId = :userId AND permission = :permission")
    suspend fun getUserPermission(userId: String, permission: String): UserPermission?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPermission(permission: UserPermission)

    @Update
    suspend fun updatePermission(permission: UserPermission)

    @Delete
    suspend fun deletePermission(permission: UserPermission)

    @Query("DELETE FROM user_permissions WHERE userId = :userId")
    suspend fun deleteUserPermissions(userId: String)
}
