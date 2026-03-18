package com.pinpoint.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinpoint.domain.model.UIGoogleUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
): ContainerHost<LoginScreenState, LoginScreenSideEffect>, ViewModel() {

    override val container: Container<LoginScreenState, LoginScreenSideEffect> = container(initialState = LoginScreenState())


    init {

    }


    fun onSignIn() {
        viewModelScope.launch {
            intent {
                reduce {
                    state.copy(
                        isLoading = true
                    )
                }
                postSideEffect(LoginScreenSideEffect.SignInWithGoogle)
            }
        }
    }

    fun saveLocalUser(userInfo: UIGoogleUserInfo) {
        viewModelScope.launch {

        }
    }

    fun onSignInCancel() {
        viewModelScope.launch {
            intent {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }


    fun createUser(
        userInfo: UIGoogleUserInfo
    ) {
        viewModelScope.launch {
            intent {
                saveLocalUser(userInfo)
                delay(500)
                postSideEffect(LoginScreenSideEffect.NavigateToHomeScreen)
            }
        }
    }
}