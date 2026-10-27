package com.reachout.app.domain.model

/**
 * Verification status of a contact or provider in the community network.
 *
 * @property displayNameKey The string resource key used for localization.
 */
enum class VerificationStatus(val displayNameKey: String) {
    VERIFIED("verification_verified"),
    UNVERIFIED("verification_unverified"),
    EXPIRED("verification_expired"),
    SUSPENDED("verification_suspended");

    companion object {
        /**
         * Safely parses a string value into a [VerificationStatus].
         * Defaults to [UNVERIFIED] if no match is found.
         *
         * @param value The name of the verification status to resolve.
         * @return The matching [VerificationStatus] or [UNVERIFIED].
         */
        fun fromString(value: String?): VerificationStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: UNVERIFIED
        }
    }
}
