package com.abhishek.zerodroid.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.zerodroid.core.database.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int = 300): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alert: AlertEntity)

    @Query("DELETE FROM alerts")
    suspend fun clearAll()
}
