package com.pinpoint.feature.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.domain.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ContainerHost<LandingScreenState, LandingScreenSideEffect>, ViewModel() {

    override val container: Container<LandingScreenState, LandingScreenSideEffect> =
        container(initialState = LandingScreenState()) {
            checkUserSession()
        }

    private fun checkUserSession() = intent {
        val savedUid = userPreferences.getUid()
        val currentUser = Firebase.auth.currentUser
        if (savedUid != null && currentUser != null) {
            postSideEffect(LandingScreenSideEffect.NavigateToMap)
        } else {
            if (savedUid != null) {
                userPreferences.clear()
            }
            postSideEffect(LandingScreenSideEffect.NavigateToLogin)
        }
    }
}
