package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventdetails

import androidx.lifecycle.ViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.DeleteEventUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetEventDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventDetailsState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    fun getEventDetails(id: Int) {
        disposable.add(
            getEventDetailsUseCase(id).subscribe { (event, relatedEventData) ->
                _uiState.value = _uiState.value.copy(
                    event = event,
                    relatedEventData = relatedEventData,
                )
            }
        )
    }
    
    fun onDelete() {
        val id = _uiState.value.event.id
        disposable.add(
            deleteEventUseCase(id).subscribe { result ->
                _uiState.value = _uiState.value.copy(
                    deleteResult = result,
                )
            }
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}