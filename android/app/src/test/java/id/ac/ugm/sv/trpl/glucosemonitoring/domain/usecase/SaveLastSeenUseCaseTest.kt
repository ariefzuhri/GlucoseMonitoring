package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeLastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar

class SaveLastSeenUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var saveLastSeenUseCase: SaveLastSeenUseCase
    private lateinit var fakeLastSeenRepository: FakeLastSeenRepository
    
    private val dummyGlucoseLevel = 100
    
    @Before
    fun setUp() {
        fakeLastSeenRepository = spyk(FakeLastSeenRepository())
        
        saveLastSeenUseCase = SaveLastSeenUseCase(
            lastSeenRepository = fakeLastSeenRepository,
        )
    }
    
    @Test
    fun `Save last seen glucose level, return complete`() {
        val observable = saveLastSeenUseCase(
            glucoseLevel = dummyGlucoseLevel,
        )
        val testObservable = observable.test()
        
        verify {
            fakeLastSeenRepository.saveLastSeen(
                glucoseLevel = eq(dummyGlucoseLevel),
                glucoseTime = any(),
            )
        }
        
        testObservable.assertComplete()
        
        confirmVerified(fakeLastSeenRepository)
    }
    
    @Test
    fun `Save last seen glucose level, the glucose time updated with current time`() {
        val observable = saveLastSeenUseCase(
            glucoseLevel = dummyGlucoseLevel,
        )
        val testObservable = observable.test()
        
        verify {
            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(Calendar.getInstance().time)
            
            fakeLastSeenRepository.saveLastSeen(
                glucoseLevel = eq(dummyGlucoseLevel),
                glucoseTime = eq(currentTime),
            )
        }
        
        testObservable.assertComplete()
        
        confirmVerified(fakeLastSeenRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}