package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.login

import androidx.lifecycle.ViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    fun onInputEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    
    fun onInputPassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    
    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        disposable.add(
            loginUseCase(
                email = email,
                password = password,
            ).subscribe { result ->
                _uiState.value = _uiState.value.copy(
                    loginResult = result,
                )
            }
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}