package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.splash

import androidx.lifecycle.ViewModel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.DownloadEventDataUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.DownloadGlucoseDataUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetLoginSessionUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getLoginSessionUseCase: GetLoginSessionUseCase,
    private val downloadGlucoseDataUseCase: DownloadGlucoseDataUseCase,
    private val downloadEventDataUseCase: DownloadEventDataUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SplashState(isLoading = true))
    val uiState = _uiState.asStateFlow()
    
    private val disposable = CompositeDisposable()
    
    init {
        observeLoginState()
    }
    
    private fun observeLoginState() {
        disposable.add(
            getLoginSessionUseCase().take(1).subscribe { isLoggedIn ->
                _uiState.value = _uiState.value.copy(isLoggedIn = isLoggedIn)
                if (isLoggedIn) {
                    initData()
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        )
    }
    
    private fun initData() {
        disposable.add(
            Flowable
                .combineLatest(
                    downloadGlucoseDataUseCase(),
                    downloadEventDataUseCase(),
                    getSettingsUseCase().take(1),
                ) { downloadGlucoseDataResult, downloadEventDataResult, settingsUseCase ->
                    val isDataDownloadCompleted = downloadGlucoseDataResult !is Result.Loading &&
                            downloadEventDataResult !is Result.Loading &&
                            downloadGlucoseDataResult !is Result.Standby &&
                            downloadEventDataResult !is Result.Standby
                    _uiState.value = _uiState.value.copy(
                        isLoading = !isDataDownloadCompleted,
                        isDownloadingData = !isDataDownloadCompleted,
                        enableGlucoseAlarms = settingsUseCase.enableGlucoseAlarms
                    )
                }
                .subscribe()
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
}