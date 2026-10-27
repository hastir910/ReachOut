package com.reachout.app

import android.app.Application
import android.content.Context
import android.util.Log
import com.reachout.app.data.local.ReachOutDatabase
import com.reachout.app.data.local.entity.ContactEntity
import com.reachout.app.data.local.entity.EmergencyGuideEntity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Main Android [Application] class for the ReachOut app.
 *
 * Configured with [@HiltAndroidApp] to trigger Hilt's code generation for dependency injection.
 * Manages initial database seeding with essential emergency guides, emergency contact numbers,
 * and sample community resources on the initial app launch.
 */
@HiltAndroidApp
class ReachOutApplication : Application() {

    @Inject
    lateinit var database: ReachOutDatabase

    override fun onCreate() {
        super.onCreate()
        seedDatabaseIfNeeded()
    }

    /**
     * Inspects [android.content.SharedPreferences] and populates default emergency guides
     * and demo contacts if this is the application's first launch.
     */
    private fun seedDatabaseIfNeeded() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isSeeded = prefs.getBoolean(KEY_DATABASE_SEEDED_V2, false)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ensure initial official dataset and places are always inserted safely
                database.contactDao().insertContacts(com.reachout.app.data.local.seed.InitialDataset.emergencyContacts)
                database.placeDao().insertPlaces(com.reachout.app.data.local.seed.InitialDataset.safePlaces)

                if (!isSeeded) {
                    val currentTime = System.currentTimeMillis()

                    // Preload critical emergency guides (National emergency numbers for India)
                    val guides = listOf(
                        EmergencyGuideEntity(
                            id = UUID.randomUUID().toString(),
                            title = "Unified Emergency Response (112)",
                            category = "GENERAL",
                            content = """
                                # National Emergency Number: 112
                                Single emergency response number across India for Police, Fire, and Ambulance services.
                                
                                ## When to Call:
                                - Immediate threat to life or property
                                - Crimes in progress or violence
                                - Road accidents and collisions
                                - Structural collapses or explosions
                                
                                ## What to Tell the Operator:
                                1. Exact location (State, District, Landmark)
                                2. Nature of emergency and number of persons affected
                                3. Your name and callback phone number
                                4. Keep calm and stay on the line until told to disconnect.
                            """.trimIndent(),
                            language = "en",
                            region = "IN",
                            emergencyNumber = "112",
                            lastUpdatedDate = currentTime,
                            isSampleData = true,
                            sortOrder = 1
                        ),
                        EmergencyGuideEntity(
                            id = UUID.randomUUID().toString(),
                            title = "Medical Emergency & First Aid (102 / 108)",
                            category = "MEDICAL",
                            content = """
                                # Medical Emergency & Ambulance (102 / 108)
                                Immediate medical protocols for acute injury or illness.
                                
                                ## Critical First Aid Steps:
                                - **Severe Bleeding**: Apply firm, continuous pressure with a clean cloth directly on the wound. Do not remove soaked cloths; place new ones on top.
                                - **Unconsciousness & Breathing**: Place individual in the recovery position (on their side) with airway tilted back.
                                - **Choking**: Perform Heimlich manoeuvre (upward abdominal thrusts) if person cannot breathe or speak.
                                - **Burns**: Cool burn under running water for at least 10 minutes. Do not apply ice, butter, or paste. Cover loosely with sterile dressing.
                                - **Snakebite**: Keep the patient still and calm to slow venom spread. Immobilise limb below heart level. Never cut, suck, or apply tourniquet. Reach hospital immediately for anti-venom.
                                
                                ## Safety Notice:
                                Call 108 or 102 immediately for ambulance dispatch before attempting transport of spinal injury victims.
                            """.trimIndent(),
                            language = "en",
                            region = "IN",
                            emergencyNumber = "108",
                            lastUpdatedDate = currentTime,
                            isSampleData = true,
                            sortOrder = 2
                        ),
                        EmergencyGuideEntity(
                            id = UUID.randomUUID().toString(),
                            title = "Police Assistance & Safety Protocol (100 / 112)",
                            category = "POLICE",
                            content = """
                                # Police Emergency (100 / 112)
                                Guidelines for safety during security incidents, harassment, or crime.
                                
                                ## Emergency Action:
                                - Dial **100** or **112** immediately.
                                - Women Helpline: **1091** or **181**.
                                - Child Helpline: **1098**.
                                
                                ## Safety Guidance:
                                - If feeling threatened in public, move towards well-lit, crowded safe places such as open shops, hospitals, or transit stations.
                                - Never share bank OTPs, PINs, or card CVVs with anyone claiming to be law enforcement or bank representatives.
                                - Note physical descriptions, vehicle registration numbers, and exact timestamps to assist police response.
                            """.trimIndent(),
                            language = "en",
                            region = "IN",
                            emergencyNumber = "100",
                            lastUpdatedDate = currentTime,
                            isSampleData = true,
                            sortOrder = 3
                        ),
                        EmergencyGuideEntity(
                            id = UUID.randomUUID().toString(),
                            title = "Fire Emergency & Evacuation (101)",
                            category = "FIRE",
                            content = """
                                # Fire Emergency & Evacuation (101)
                                Protocol for residential, electrical, and commercial fires.
                                
                                ## Immediate Protocol:
                                1. Shout "FIRE!" and alert all occupants immediately.
                                2. Dial **101** or **112** for Fire Brigade dispatch.
                                3. Evacuate via the nearest stairs. **NEVER use elevators**.
                                4. If smoke is present, stay low and crawl on hands and knees where air is cleaner.
                                5. Feel doors with the back of your hand before opening. If hot, do not open.
                                6. If clothing catches fire: **STOP, DROP, and ROLL**.
                                
                                ## Electrical Fires:
                                - Switch off main circuit breaker if safe to do so.
                                - Never use water on electrical fires or burning cooking oil. Use sand, baking soda, or ABC dry chemical fire extinguisher.
                            """.trimIndent(),
                            language = "en",
                            region = "IN",
                            emergencyNumber = "101",
                            lastUpdatedDate = currentTime,
                            isSampleData = true,
                            sortOrder = 4
                        ),
                        EmergencyGuideEntity(
                            id = UUID.randomUUID().toString(),
                            title = "Natural Disaster & Flood Safety (1070 / 1077)",
                            category = "NATURAL_DISASTER",
                            content = """
                                # Disaster Helpline (1070 State / 1077 District)
                                Offline safety checklist for floods, cyclones, and earthquakes.
                                
                                ## Flood Preparedness:
                                - Disconnect gas and electricity mains before floodwaters enter.
                                - Never walk or drive through flowing water; six inches of water can knock you down.
                                - Boil all drinking water or use water purification tablets.
                                
                                ## Earthquake Protocol:
                                - **DROP, COVER, HOLD ON**: Drop under a sturdy table, protect head and neck, hold on until shaking stops.
                                - If outdoors, move to open ground away from buildings, power lines, and trees.
                            """.trimIndent(),
                            language = "en",
                            region = "IN",
                            emergencyNumber = "1077",
                            lastUpdatedDate = currentTime,
                            isSampleData = true,
                            sortOrder = 5
                        )
                    )
                    database.emergencyGuideDao().insertGuides(guides)

                    // Preload sample community contacts clearly marked as demo/sample data
                    val sampleContacts = listOf(
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] City General Hospital Emergency",
                            serviceCategory = "HOSPITAL",
                            contactNumber = "102",
                            alternateNumber = "+91-11-23456789",
                            address = "Civic Centre Road, Sector 4",
                            landmark = "Opposite Public Gardens",
                            area = "Central District",
                            locality = "Civil Lines",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "24/7 Emergency & Trauma",
                            servicesProvided = "Emergency, Trauma ICU, Blood Bank, Ambulance",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "District Health Office",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "National Health Mission Directory",
                            availabilityInfo = "Active 24/7 emergency admissions",
                            latitude = 28.6139,
                            longitude = 77.2090,
                            isSaved = true,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        ),
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] Central Police Station & Helpdesk",
                            serviceCategory = "POLICE",
                            contactNumber = "100",
                            alternateNumber = "112",
                            address = "Station Road, Old Market",
                            landmark = "Near Clock Tower",
                            area = "Market Square",
                            locality = "Old City",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "24/7 Active Duty",
                            servicesProvided = "Emergency Police Response, Patrol, First Aid, Women Safety",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "City Police Commissionerate",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "State Police Portal",
                            availabilityInfo = "Mobile patrol units on call",
                            latitude = 28.6250,
                            longitude = 77.2150,
                            isSaved = true,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        ),
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] Fire & Rescue Brigade Division",
                            serviceCategory = "FIRE",
                            contactNumber = "101",
                            alternateNumber = "112",
                            address = "Ring Road bypass",
                            landmark = "Adjacent Industrial Estate",
                            area = "Phase 1",
                            locality = "East Sector",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "24/7 Fire & Disaster Response",
                            servicesProvided = "Firefighting, Flood Rescue, Hazardous Spills",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "Fire Services Directorate",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "Fire Services Dept",
                            availabilityInfo = "Quick response vehicles stationed",
                            latitude = 28.6300,
                            longitude = 77.2200,
                            isSaved = false,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        ),
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] 24/7 Community Care Pharmacy",
                            serviceCategory = "PHARMACY",
                            contactNumber = "+91-9876543210",
                            alternateNumber = null,
                            address = "Shop 12, Main Bazaar",
                            landmark = "Behind Bus Terminal",
                            area = "Downtown",
                            locality = "Town Center",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "24 Hours Open",
                            servicesProvided = "Essential Medicines, First Aid Kits, Glucose, Insulin",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "Pharmacists Association",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "Community Verified",
                            availabilityInfo = "Emergency medicines in stock",
                            latitude = 28.6180,
                            longitude = 77.2120,
                            isSaved = false,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        ),
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] Kisan Call Centre & Agro Assistance",
                            serviceCategory = "AGRICULTURAL",
                            contactNumber = "18001801551",
                            alternateNumber = "+91-11-23381012",
                            address = "Krishi Vigyan Kendra Complex",
                            landmark = "Near State Grain Depot",
                            area = "Rural Outpost",
                            locality = "Agro Cluster",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "06:00 - 22:00 (All Days)",
                            servicesProvided = "Crops advisory, Weather warnings, Veterinary support",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "Ministry of Agriculture",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "Kisan Portal",
                            availabilityInfo = "Toll-free specialist hotline",
                            latitude = 28.5800,
                            longitude = 77.1800,
                            isSaved = false,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        ),
                        ContactEntity(
                            id = UUID.randomUUID().toString(),
                            name = "[SAMPLE] Highway Breakdown & Towing Service",
                            serviceCategory = "MECHANIC",
                            contactNumber = "+91-9876501234",
                            alternateNumber = "+91-9876505678",
                            address = "National Highway Exit 9",
                            landmark = "Next to Fuel Station",
                            area = "Highway Corridor",
                            locality = "Outer Ring",
                            district = "Central",
                            state = "Delhi",
                            operatingHours = "24/7 Roadside Assistance",
                            servicesProvided = "Vehicle towing, Tyre puncture repair, Battery jumpstart",
                            verificationStatus = "VERIFIED",
                            verifiedBy = "Automobile Association",
                            verificationDate = currentTime,
                            lastUpdatedDate = currentTime,
                            source = "Highway Authority Listed",
                            availabilityInfo = "Towing van available within 20km",
                            latitude = 28.6400,
                            longitude = 77.2400,
                            isSaved = false,
                            isSampleData = true,
                            syncStatus = "SYNCED"
                        )
                    )
                    database.contactDao().insertContacts(sampleContacts)
                    database.contactDao().insertContacts(com.reachout.app.data.local.seed.InitialDataset.emergencyContacts)
                    database.placeDao().insertPlaces(com.reachout.app.data.local.seed.InitialDataset.safePlaces)

                    // Mark database as seeded
                    prefs.edit().putBoolean(KEY_DATABASE_SEEDED_V2, true).apply()
                    Log.i(TAG, "ReachOut database seeded successfully with guides, contacts, and safe places.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error seeding initial database content", e)
            }
        }
    }

    companion object {
        private const val TAG = "ReachOutApplication"
        private const val PREFS_NAME = "reachout_prefs"
        private const val KEY_DATABASE_SEEDED = "database_seeded"
        private const val KEY_DATABASE_SEEDED_V2 = "database_seeded_v2"
    }
}
