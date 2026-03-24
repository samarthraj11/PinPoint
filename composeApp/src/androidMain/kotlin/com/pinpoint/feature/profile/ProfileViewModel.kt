package com.pinpoint.feature.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.domain.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ContainerHost<ProfileScreenState, ProfileScreenSideEffect>, ViewModel() {

    override val container: Container<ProfileScreenState, ProfileScreenSideEffect> =
        container(initialState = ProfileScreenState())

    fun logout() = intent {
        Firebase.auth.signOut()
        userPreferences.clear()
        postSideEffect(ProfileScreenSideEffect.NavigateToLogin)
    }
}

data class ProfileScreenState(
    val placeholder: Boolean = false
)

sealed interface ProfileScreenSideEffect {
    data object NavigateToLogin : ProfileScreenSideEffect
}
