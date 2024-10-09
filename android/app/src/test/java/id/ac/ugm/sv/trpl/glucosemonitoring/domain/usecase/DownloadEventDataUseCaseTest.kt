package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
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

class DownloadEventDataUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var downloadEventDataUseCase: DownloadEventDataUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        
        downloadEventDataUseCase = DownloadEventDataUseCase(
            eventRepository = fakeEventRepository,
            userRepository = FakeUserRepository(),
        )
    }
    
    @Test
    fun `Update event data from remote, return success`() {
        val observable = downloadEventDataUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeEventRepository.downloadEventData(any())
        }
        
        val expectedResult = Result.Success<Nothing>()
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeEventRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}