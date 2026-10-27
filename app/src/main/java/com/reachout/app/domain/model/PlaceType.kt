package com.reachout.app.domain.model

/**
 * Types of physical places and community landmarks.
 *
 * @property displayNameKey The string resource key used for localization.
 */
enum class PlaceType(val displayNameKey: String) {
    HOSPITAL("place_type_hospital"),
    CLINIC("place_type_clinic"),
    PHARMACY("place_type_pharmacy"),
    POLICE_STATION("place_type_police_station"),
    FIRE_STATION("place_type_fire_station"),
    EMERGENCY_FACILITY("place_type_emergency_facility"),
    COMMUNITY_CENTRE("place_type_community_centre"),
    NGO_CENTRE("place_type_ngo_centre"),
    SHELTER("place_type_shelter"),
    COMMUNITY_ASSISTANCE_POINT("place_type_community_assistance_point"),
    OTHER("place_type_other");

    companion object {
        /**
         * Safely parses a string value into a [PlaceType].
         * Defaults to [OTHER] if no match is found.
         *
         * @param value The name of the place type to resolve.
         * @return The matching [PlaceType] or [OTHER].
         */
        fun fromString(value: String?): PlaceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}
