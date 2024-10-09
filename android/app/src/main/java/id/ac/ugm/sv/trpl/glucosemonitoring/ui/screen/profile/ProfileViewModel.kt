package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.ChangeSettingsUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetHealthNumbersUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetProfileUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetSettingsUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.LogoutUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getHealthNumbersUseCase: GetHealthNumbersUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val changeSettingsUseCase: ChangeSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        observeProfile()
        observeHealthNumbers()
        observeSettings()
    }
    
    private fun observeProfile() {
        disposable.add(
            getProfileUseCase().subscribe { profile ->
                _uiState.value = _uiState.value.copy(
                    name = profile.name,
                    email = profile.email,
                )
            }
        )
    }
    
    private fun observeHealthNumbers() {
        disposable.add(
            getHealthNumbersUseCase().subscribe { healthNumbers ->
                _uiState.value = _uiState.value.copy(
                    healthNumbers = healthNumbers,
                )
            }
        )
    }
    
    private fun observeSettings() {
        disposable.add(
            getSettingsUseCase().subscribe { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                )
            }
        )
    }
    
    fun onEnableGlucoseAlarmsChange(isEnabled: Boolean) {
        disposable.add(
            changeSettingsUseCase(
                enableGlucoseAlarms = isEnabled,
            ).subscribe()
        )
    }
    
    fun logout() {
        disposable.add(
            logoutUseCase().subscribe()
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}