package com.abhishek.zerodroid.core.alerts

import com.abhishek.zerodroid.core.database.dao.AlertDao
import com.abhishek.zerodroid.core.database.entity.AlertEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared sink every detector ViewModel (Rogue AP, Deauth, GPS Spoof, Hidden
 * Camera, Bluetooth Tracker) writes newly-detected threats into, and the
 * Alert Center screen / Dashboard summary card read from. Each source is
 * responsible for its own de-duplication before calling [record] - this
 * repository does not try to detect "is this the same threat as before".
 */
@Singleton
class AlertCenterRepository @Inject constructor(
    private val alertDao: AlertDao
) {
    val alerts: Flow<List<UnifiedAlert>> = alertDao.observeRecent().map { entities ->
        entities.mapNotNull { it.toUnifiedAlertOrNull() }
    }

    suspend fun record(
        source: AlertSource,
        severity: AlertSeverity,
        title: String,
        detail: String,
        timestamp: Long = System.currentTimeMillis()
    ) {
        alertDao.insert(
            AlertEntity(
                id = UUID.randomUUID().toString(),
                source = source.name,
                severity = severity.name,
                title = title,
                detail = detail,
                timestamp = timestamp
            )
        )
    }

    suspend fun clearAll() = alertDao.clearAll()

    private fun AlertEntity.toUnifiedAlertOrNull(): UnifiedAlert? {
        val source = runCatching { AlertSource.valueOf(source) }.getOrNull() ?: return null
        val severity = runCatching { AlertSeverity.valueOf(severity) }.getOrNull() ?: return null
        return UnifiedAlert(
            id = id,
            source = source,
            severity = severity,
            title = title,
            detail = detail,
            timestamp = timestamp
        )
    }
}
