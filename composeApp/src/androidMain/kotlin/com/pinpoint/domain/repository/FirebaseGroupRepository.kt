package com.pinpoint.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.pinpoint.domain.model.Group
import com.pinpoint.domain.model.GroupMember
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseGroupRepository @Inject constructor() {

    private val database = FirebaseDatabase.getInstance()
    private val groupsRef = database.getReference("groups")

    suspend fun createGroup(
        name: String,
        creatorUid: String,
        creatorName: String,
        creatorPhotoUrl: String?
    ): String {
        val groupId = groupsRef.push().key ?: throw Exception("Failed to generate group ID")
        val groupData = mapOf<String, Any?>(
            "name" to name,
            "createdBy" to creatorUid,
            "createdByName" to creatorName,
            "createdAt" to ServerValue.TIMESTAMP,
            "members/$creatorUid" to mapOf(
                "displayName" to creatorName,
                "photoUrl" to creatorPhotoUrl,
                "joinedAt" to ServerValue.TIMESTAMP
            )
        )
        groupsRef.child(groupId).updateChildren(groupData.filterValues { it != null } as Map<String, Any>).await()
        return groupId
    }

    suspend fun joinGroup(
        groupId: String,
        uid: String,
        displayName: String,
        photoUrl: String?
    ): Boolean {
        val snapshot = groupsRef.child(groupId).child("name").get().await()
        if (!snapshot.exists()) return false

        val memberData = mutableMapOf<String, Any>(
            "displayName" to displayName,
            "joinedAt" to ServerValue.TIMESTAMP
        )
        photoUrl?.let { memberData["photoUrl"] = it }
        groupsRef.child(groupId).child("members").child(uid).updateChildren(memberData).await()
        return true
    }

    fun observeUserGroups(uid: String): Flow<List<Group>> = callbackFlow {
        val listener = groupsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groups = snapshot.children.mapNotNull { groupSnap ->
                    val membersSnap = groupSnap.child("members")
                    if (!membersSnap.hasChild(uid)) return@mapNotNull null

                    val members = membersSnap.children.associate { memberSnap ->
                        val memberUid = memberSnap.key ?: ""
                        memberUid to GroupMember(
                            uid = memberUid,
                            displayName = memberSnap.child("displayName").getValue(String::class.java) ?: "",
                            photoUrl = memberSnap.child("photoUrl").getValue(String::class.java),
                            joinedAt = memberSnap.child("joinedAt").getValue(Long::class.java) ?: 0L
                        )
                    }

                    Group(
                        id = groupSnap.key ?: "",
                        name = groupSnap.child("name").getValue(String::class.java) ?: "",
                        createdBy = groupSnap.child("createdBy").getValue(String::class.java) ?: "",
                        createdByName = groupSnap.child("createdByName").getValue(String::class.java) ?: "",
                        createdAt = groupSnap.child("createdAt").getValue(Long::class.java) ?: 0L,
                        memberCount = membersSnap.childrenCount.toInt(),
                        members = members
                    )
                }
                trySend(groups)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { groupsRef.removeEventListener(listener) }
    }

    suspend fun leaveGroup(groupId: String, uid: String) {
        groupsRef.child(groupId).child("members").child(uid).removeValue().await()
    }
}
