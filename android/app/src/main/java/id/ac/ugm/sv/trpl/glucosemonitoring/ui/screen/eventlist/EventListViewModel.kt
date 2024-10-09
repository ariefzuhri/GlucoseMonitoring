package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetEventListUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventListUseCase: GetEventListUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventListState())
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        observeEventData()
    }
    
    private fun observeEventData() {
        disposable.add(
            getEventListUseCase().subscribe { eventData ->
                _uiState.value = _uiState.value.copy(
                    eventData = eventData.reversed(),
                )
            }
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}