package com.reachout.app.domain.model

/**
 * Categories of services and resources available in the community.
 *
 * @property displayNameKey The string resource key used for localization.
 */
enum class ServiceCategory(val displayNameKey: String) {
    HOSPITAL("category_hospital"),
    CLINIC("category_clinic"),
    PHARMACY("category_pharmacy"),
    AMBULANCE("category_ambulance"),
    POLICE("category_police"),
    FIRE("category_fire"),
    TRANSPORT("category_transport"),
    MECHANIC("category_mechanic"),
    AGRICULTURAL("category_agricultural"),
    VOLUNTEER("category_volunteer"),
    NGO("category_ngo"),
    COMMUNITY_CENTRE("category_community_centre"),
    WATER_POINT("category_water_point"),
    SHELTER("category_shelter"),
    FOOD_ASSISTANCE("category_food_assistance"),
    OTHER("category_other");

    companion object {
        /**
         * Safely parses a string value into a [ServiceCategory].
         * Defaults to [OTHER] if no match is found.
         *
         * @param value The name of the category to resolve.
         * @return The matching [ServiceCategory] or [OTHER].
         */
        fun fromString(value: String?): ServiceCategory {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}
