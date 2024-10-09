package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.healthnumbersform

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toStringOrEmpty
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetHealthNumbersUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.UpdateHealthNumbersUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HealthNumbersFormViewModel @Inject constructor(
    private val getHealthNumbersUseCase: GetHealthNumbersUseCase,
    private val updateHealthNumbersUseCase: UpdateHealthNumbersUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HealthNumbersFormState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        observeHealthNumbers()
    }
    
    private fun observeHealthNumbers() {
        disposable.add(
            getHealthNumbersUseCase().subscribe { healthNumbers ->
                _uiState.value = HealthNumbersFormState(
                    weight = healthNumbers.weight.toStringOrEmpty(),
                    height = healthNumbers.height.toStringOrEmpty(),
                    systolic = healthNumbers.systolic.toStringOrEmpty(),
                    diastolic = healthNumbers.diastolic.toStringOrEmpty(),
                )
            }
        )
    }
    
    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(
            weight = weight,
        )
    }
    
    fun onHeightChange(height: String) {
        _uiState.value = _uiState.value.copy(
            height = height,
        )
    }
    
    fun onSystolicChange(systolic: String) {
        _uiState.value = _uiState.value.copy(
            systolic = systolic,
        )
    }
    
    fun onDiastolicChange(diastolic: String) {
        _uiState.value = _uiState.value.copy(
            diastolic = diastolic,
        )
    }
    
    fun save() {
        val weight = _uiState.value.weight.toIntOrNull()
        val height = _uiState.value.height.toIntOrNull()
        val systolic = _uiState.value.systolic.toIntOrNull()
        val diastolic = _uiState.value.diastolic.toIntOrNull()
        
        disposable.add(
            updateHealthNumbersUseCase(
                weight = weight,
                height = height,
                systolic = systolic,
                diastolic = diastolic,
            ).subscribe()
        )
    }
    
}