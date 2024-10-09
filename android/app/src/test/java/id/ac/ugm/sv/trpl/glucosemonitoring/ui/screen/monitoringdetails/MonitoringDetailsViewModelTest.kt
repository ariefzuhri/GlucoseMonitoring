package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetMonitoringDetailsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test

class MonitoringDetailsViewModelTest {
    
    private lateinit var monitoringDetailsViewModel: MonitoringDetailsViewModel
    private lateinit var getMonitoringDetailsUseCase: GetMonitoringDetailsUseCase
    
    @Before
    fun setUp() {
        getMonitoringDetailsUseCase = mockk()
        
        monitoringDetailsViewModel = MonitoringDetailsViewModel(
            getMonitoringDetailsUseCase = getMonitoringDetailsUseCase,
        )
    }
    
    @Test
    fun `Get details, return details`() {
        every { getMonitoringDetailsUseCase(any(), any()) } returns Flowable.just(
            MonitoringDetails(
                glucoseData = listOf(),
                eventData = listOf(),
                glucoseStats = MonitoringDetails.GlucoseStats(
                    lastValue = 0,
                    average = 0,
                    max = 0,
                    min = 0,
                ),
                tir = MonitoringDetails.Tir(
                    timeInRange = 0,
                    timeAboveRange = 0,
                    timeBelowRange = 0,
                ),
                recommendations = Recommendations.Builder()
                    .provide()
            )
        )
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}