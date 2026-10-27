package com.reachout.app.domain.model

/**
 * Types of assistance that community members can request or offer.
 *
 * @property displayNameKey The string resource key used for localization.
 * @property iconName The icon asset/name corresponding to this assistance type.
 */
enum class AssistanceType(val displayNameKey: String, val iconName: String) {
    MEDICAL("assistance_type_medical", "ic_medical"),
    TRANSPORT("assistance_type_transport", "ic_transport"),
    AGRICULTURAL("assistance_type_agricultural", "ic_agriculture"),
    VEHICLE_BREAKDOWN("assistance_type_vehicle_breakdown", "ic_car_repair"),
    COMMUNITY_SUPPORT("assistance_type_community_support", "ic_community"),
    EMERGENCY("assistance_type_emergency", "ic_emergency"),
    OTHER("assistance_type_other", "ic_help");

    companion object {
        /**
         * Safely parses a string value into an [AssistanceType].
         * Defaults to [OTHER] if no match is found.
         *
         * @param value The name of the assistance type to resolve.
         * @return The matching [AssistanceType] or [OTHER].
         */
        fun fromString(value: String?): AssistanceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}
