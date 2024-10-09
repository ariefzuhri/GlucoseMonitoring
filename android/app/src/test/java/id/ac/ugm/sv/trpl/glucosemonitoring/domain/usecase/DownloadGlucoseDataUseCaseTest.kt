package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DownloadGlucoseDataUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var downloadGlucoseDataUseCase: DownloadGlucoseDataUseCase
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    
    @Before
    fun setUp() {
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        
        downloadGlucoseDataUseCase = DownloadGlucoseDataUseCase(
            glucoseRepository = fakeGlucoseRepository,
            userRepository = FakeUserRepository(),
        )
    }
    
    @Test
    fun `Update glucose data from remote, return success dan the data updated`() {
        val observable = downloadGlucoseDataUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeGlucoseRepository.downloadGlucoseData(any())
        }
        
        val expectedResult = Result.Success<Nothing>()
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}