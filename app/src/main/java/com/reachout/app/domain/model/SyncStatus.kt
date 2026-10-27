package com.reachout.app.domain.model

/**
 * Synchronization state of locally stored entities with the remote server.
 */
enum class SyncStatus {
    SYNCED,
    PENDING_SYNC,
    LOCAL_ONLY;

    companion object {
        /**
         * Safely parses a string value into a [SyncStatus].
         * Defaults to [LOCAL_ONLY] if no match is found.
         *
         * @param value The name of the sync status to resolve.
         * @return The matching [SyncStatus] or [LOCAL_ONLY].
         */
        fun fromString(value: String?): SyncStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: LOCAL_ONLY
        }
    }
}
