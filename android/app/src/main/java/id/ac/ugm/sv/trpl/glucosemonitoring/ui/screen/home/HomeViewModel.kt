package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetMonitoringSummaryUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetProfileUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.SaveLastSeenUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonitoringSummaryUseCase: GetMonitoringSummaryUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveLastSeenUseCase: SaveLastSeenUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        observeMonitoringSummary()
        observeProfile()
    }
    
    private fun observeMonitoringSummary() {
        fun saveLastSeen(glucoseLevel: Int?) {
            disposable.add(
                saveLastSeenUseCase(glucoseLevel).subscribe()
            )
        }
        
        disposable.add(
            getMonitoringSummaryUseCase().subscribe { monitoringSummary ->
                _uiState.value = _uiState.value.copy(
                    monitoringSummary = monitoringSummary,
                )
                saveLastSeen(monitoringSummary.latestGlucoseLevel)
            }
        )
    }
    
    private fun observeProfile() {
        disposable.add(
            getProfileUseCase().subscribe { profile ->
                _uiState.value = _uiState.value.copy(
                    userName = profile.name,
                )
            }
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}