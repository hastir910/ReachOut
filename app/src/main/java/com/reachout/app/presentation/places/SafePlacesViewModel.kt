package com.reachout.app.presentation.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reachout.app.data.local.entity.AssistanceRequestEntity
import com.reachout.app.data.local.entity.PlaceEntity
import com.reachout.app.data.remote.firebase.FirebaseSyncManager
import com.reachout.app.domain.repository.AssistanceRequestRepository
import com.reachout.app.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel managing safe places search, category filtering, bookmarking,
 * and user help requests.
 */
@HiltViewModel
class SafePlacesViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val assistanceRequestRepository: AssistanceRequestRepository,
    private val firebaseSyncManager: FirebaseSyncManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("ALL")
    val showSavedOnly = MutableStateFlow(false)

    val places: StateFlow<List<PlaceEntity>> = combine(
        searchQuery,
        selectedCategory,
        showSavedOnly
    ) { query, category, savedOnly ->
        Triple(query, category, savedOnly)
    }.flatMapLatest { (query, category, savedOnly) ->
        if (query.isNotBlank()) {
            placeRepository.searchPlaces(query.trim())
        } else if (savedOnly) {
            placeRepository.getSavedPlaces()
        } else if (category != "ALL") {
            placeRepository.getPlacesByType(category)
        } else {
            placeRepository.getAllPlaces()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        selectedCategory.value = category
    }

    fun toggleSavedOnly() {
        showSavedOnly.value = !showSavedOnly.value
    }

    fun toggleBookmark(place: PlaceEntity) {
        viewModelScope.launch {
            placeRepository.updateSavedStatus(place.id, !place.isSaved)
        }
    }

    /**
     * Submits an emergency/assistance request locally in Room and syncs to Firebase Firestore.
     */
    fun submitHelpRequest(
        customerName: String,
        contactNumber: String,
        assistanceType: String,
        description: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            val now = System.currentTimeMillis()
            val request = AssistanceRequestEntity(
                id = id,
                assistanceType = assistanceType,
                description = description,
                contactName = customerName.ifBlank { "Community Member" },
                contactNumber = contactNumber,
                status = "PENDING_SYNCHRONISATION",
                createdAt = now,
                updatedAt = now,
                syncStatus = "LOCAL_ONLY"
            )

            // 1. Insert into local Room database
            assistanceRequestRepository.insertRequest(request)

            // 2. Sync to Firebase Firestore
            val result = firebaseSyncManager.uploadAssistanceRequest(request)
            if (result.isSuccess) {
                assistanceRequestRepository.updateRequest(
                    request.copy(syncStatus = "SYNCED", status = "SENT_AWAITING_RESPONSE")
                )
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }
}
