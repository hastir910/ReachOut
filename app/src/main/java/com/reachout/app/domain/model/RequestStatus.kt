package com.reachout.app.domain.model

/**
 * Lifecycle status of an assistance request.
 *
 * @property displayNameKey The string resource key used for localization.
 */
enum class RequestStatus(val displayNameKey: String) {
    DRAFT("request_status_draft"),
    SAVED_OFFLINE_NOT_SENT("request_status_saved_offline"),
    PENDING_SYNCHRONISATION("request_status_pending_sync"),
    SENT_AWAITING_RESPONSE("request_status_sent_awaiting_response"),
    ACCEPTED_BY_HELPER("request_status_accepted"),
    COMPLETED("request_status_completed"),
    FAILED_TO_SEND("request_status_failed");

    companion object {
        /**
         * Safely parses a string value into a [RequestStatus].
         * Defaults to [DRAFT] if no match is found.
         *
         * @param value The name of the request status to resolve.
         * @return The matching [RequestStatus] or [DRAFT].
         */
        fun fromString(value: String?): RequestStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: DRAFT
        }
    }
}
