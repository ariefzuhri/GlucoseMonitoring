package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventform

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toDate
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.AddEventUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.EditEventUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.SelectEventUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.eventTypeOptions
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EventFormViewModel @Inject constructor(
    private val selectEventUseCase: SelectEventUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val editEventUseCase: EditEventUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventFormState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    fun selectEvent(id: Int?) {
        id?.let {
            disposable.add(
                selectEventUseCase(id).subscribe { event ->
                    _uiState.value = EventFormState(
                        isEdit = true,
                        id = event.id,
                        type = event.type,
                        desc = event.desc,
                        date = event.date.toDate(DateFormat.RAW_DATE),
                        time = event.time.toTime(DateFormat.RAW_TIME),
                        typeOptions = eventTypeOptions.map { option ->
                            option.isEnabled.value = option.name == event.type.id
                            option
                        },
                    )
                }
            )
        }
    }
    
    fun onDescChange(desc: String) {
        _uiState.value = _uiState.value.copy(desc = desc)
    }
    
    fun onTypeChange(selectedOption: Option) {
        _uiState.value = _uiState.value.copy(
            type = EventType.of(selectedOption.name),
            typeOptions = eventTypeOptions.map { option ->
                option.isEnabled.value = option.name == selectedOption.name
                option
            },
        )
    }
    
    fun onSelectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            date = date,
        )
    }
    
    fun onSelectTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(
            time = time,
        )
    }
    
    fun onSave() {
        _uiState.value.apply {
            val date = date.toString(DateFormat.RAW_DATE)
            val time = time.toString(DateFormat.RAW_TIME)
            
            disposable.add(
                if (!isEdit) {
                    addEventUseCase(
                        type = type,
                        desc = desc,
                        date = date,
                        time = time,
                    ).subscribe { result ->
                        _uiState.value = _uiState.value.copy(
                            addResult = result,
                        )
                    }
                } else {
                    editEventUseCase(
                        id = id!!,
                        type = type,
                        desc = desc,
                        date = date,
                        time = time,
                    ).subscribe { result ->
                        _uiState.value = _uiState.value.copy(
                            editResult = result,
                        )
                    }
                }
            )
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}