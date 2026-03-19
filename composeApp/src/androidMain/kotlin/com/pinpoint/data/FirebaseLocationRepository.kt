package com.pinpoint.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.pinpoint.data.model.MemberLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLocationRepository @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val activeTripRef = database.getReference("trips/active/members")

    fun updateMyLocation(uid: String, displayName: String, lat: Double, lng: Double) {
        val data = mapOf(
            "displayName" to displayName,
            "latitude" to lat,
            "longitude" to lng,
            "timestamp" to ServerValue.TIMESTAMP
        )
        activeTripRef.child(uid).setValue(data)
    }

    fun observeMembers(): Flow<List<MemberLocation>> = callbackFlow {
        val listener = activeTripRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val members = snapshot.children.mapNotNull { child ->
                    val displayName = child.child("displayName").getValue(String::class.java) ?: ""
                    val latitude = child.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = child.child("longitude").getValue(Double::class.java) ?: 0.0
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
        awaitClose { activeTripRef.removeEventListener(listener) }
    }

    fun removeMyLocation(uid: String) {
        activeTripRef.child(uid).removeValue()
    }
}
