package com.pinpoint.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.pinpoint.domain.model.MemberLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLocationRepository @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()

    private fun groupMembersRef(groupId: String) =
        database.getReference("groups/$groupId/members")

    fun updateMyLocation(
        uid: String,
        displayName: String,
        lat: Double,
        lng: Double,
        groupId: String
    ) {
        val data = mapOf<String, Any>(
            "displayName" to displayName,
            "latitude" to lat,
            "longitude" to lng,
            "timestamp" to ServerValue.TIMESTAMP
        )
        groupMembersRef(groupId).child(uid).updateChildren(data)
    }

    fun observeMembers(groupId: String): Flow<List<MemberLocation>> =
        callbackFlow {
            val ref = groupMembersRef(groupId)
            val listener = ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val members = snapshot.children.mapNotNull { child ->
                        val displayName =
                            child.child("displayName").getValue(String::class.java) ?: ""
                        val latitude = child.child("latitude").getValue(Double::class.java)
                            ?: return@mapNotNull null
                        val longitude = child.child("longitude").getValue(Double::class.java)
                            ?: return@mapNotNull null
                        val timestamp = child.child("timestamp").getValue(Long::class.java) ?: 0L
                        MemberLocation(
                            uid = child.key ?: "",
                            displayName = displayName,
                            latitude = latitude,
                            longitude = longitude,
                            timestamp = timestamp
                        )
                    }
                    trySend(members)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { ref.removeEventListener(listener) }
        }

    fun removeMyLocation(uid: String, groupId: String) {
        groupMembersRef(groupId).child(uid).removeValue()
    }
}
