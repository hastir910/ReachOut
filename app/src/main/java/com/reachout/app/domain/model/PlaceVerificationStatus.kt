package com.reachout.app.domain.model

/**
 * Verification status of a physical place or facility.
 *
 * @property displayNameKey The string resource key used for localization.
 */
enum class PlaceVerificationStatus(val displayNameKey: String) {
    VERIFIED_FACILITY("place_status_verified_facility"),
    VERIFIED_COMMUNITY_HELP_POINT("place_status_verified_community_help_point"),
    UNVERIFIED_LISTING("place_status_unverified_listing"),
    NOT_RECENTLY_CONFIRMED("place_status_not_recently_confirmed");

    companion object {
        /**
         * Safely parses a string value into a [PlaceVerificationStatus].
         * Defaults to [UNVERIFIED_LISTING] if no match is found.
         *
         * @param value The name of the place verification status to resolve.
         * @return The matching [PlaceVerificationStatus] or [UNVERIFIED_LISTING].
         */
        fun fromString(value: String?): PlaceVerificationStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: UNVERIFIED_LISTING
        }
    }
}
