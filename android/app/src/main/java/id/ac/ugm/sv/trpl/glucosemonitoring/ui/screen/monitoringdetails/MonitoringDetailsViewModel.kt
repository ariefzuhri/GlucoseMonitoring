package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.MonitoringDetailsPeriodFilter
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetMonitoringDetailsUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.monitoringDetailsPeriodFilters
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MonitoringDetailsViewModel @Inject constructor(
    private val getMonitoringDetailsUseCase: GetMonitoringDetailsUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MonitoringDetailsState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        getMonitoringDetails()
    }
    
    private fun getMonitoringDetails() {
        val selectedPeriodFilter =
            MonitoringDetailsPeriodFilter.of(_uiState.value.selectedPeriodFilter.name)
        val customPeriod = _uiState.value.customPeriod
        disposable.add(
            getMonitoringDetailsUseCase(
                selectedPeriodFilter = selectedPeriodFilter,
                customPeriod = customPeriod,
            ).subscribe { monitoringDetails ->
                _uiState.value = _uiState.value.copy(
                    glucoseData = monitoringDetails.glucoseData,
                    eventData = monitoringDetails.eventData,
                    glucoseStats = monitoringDetails.glucoseStats,
                    tir = monitoringDetails.tir,
                    recommendations = monitoringDetails.recommendations,
                )
            }
        )
    }
    
    fun onSelectFilter(selectedFilter: Option) {
        _uiState.value = _uiState.value.copy(
            periodFilters = monitoringDetailsPeriodFilters.map { filter ->
                filter.isEnabled.value = filter.name == selectedFilter.name
                filter
            }
        )
        getMonitoringDetails()
    }
    
    fun onSelectCustomPeriod(startDate: LocalDate, endDate: LocalDate) {
        _uiState.value = _uiState.value.copy(
            customPeriod = startDate..endDate,
            periodFilters = monitoringDetailsPeriodFilters.map { filter ->
                filter.isEnabled.value = filter.name == MonitoringDetailsPeriodFilter.CUSTOM.name
                filter
            },
        )
        getMonitoringDetails()
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}