//package com.example.tutorlog.feature.login
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.tutorlog.domain.PreferencesManager
//import com.example.tutorlog.domain.local.UIGoogleUserInfo
//import com.example.tutorlog.domain.usecase.RGetHomeScreenContentUseCase
//import com.example.tutorlog.domain.usecase.RGetLoginUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.orbitmvi.orbit.Container
//import org.orbitmvi.orbit.ContainerHost
//import org.orbitmvi.orbit.viewmodel.container
//import javax.inject.Inject
//
//
//@HiltViewModel
//class LoginViewModel @Inject constructor(
//    private val getLoginUseCase: RGetLoginUseCase,
//    private val preferencesManager: PreferencesManager,
//): ContainerHost<LoginScreenState, LoginScreenSideEffect>, ViewModel() {
//
//    override val container: Container<LoginScreenState, LoginScreenSideEffect> = container(initialState = LoginScreenState())
//
//
//    init {
//
//        println("karl email: ${preferencesManager.getString("user_email")} ")
//        println("karl display : ${preferencesManager.getString("user_display_name")} ")
//    }
//
//
//    fun onSignIn() {
//        viewModelScope.launch {
//            intent {
//                reduce {
//                    state.copy(
//                        isLoading = true
//                    )
//                }
//                postSideEffect(LoginScreenSideEffect.SignInWithGoogle)
//            }
//        }
//    }
//
//    fun saveLocalUser(userInfo: UIGoogleUserInfo) {
//        viewModelScope.launch {
//            preferencesManager.saveString(
//                key = "user_email",
//                value = userInfo.email
//            )
//            preferencesManager.saveString(
//                key = "user_display_name",
//                value = userInfo.displayName
//            )
//        }
//    }
//
//    fun onSignInCancel() {
//        viewModelScope.launch {
//            intent {
//                reduce {
//                    state.copy(
//                        isLoading = false
//                    )
//                }
//            }
//        }
//    }
//
//
//    fun createUser(
//        userInfo: UIGoogleUserInfo
//    ) {
//        viewModelScope.launch {
//            intent {
//                saveLocalUser(userInfo)
//                delay(500)
//                postSideEffect(LoginScreenSideEffect.NavigateToHomeScreen)
//            }
//        }
//    }
//}