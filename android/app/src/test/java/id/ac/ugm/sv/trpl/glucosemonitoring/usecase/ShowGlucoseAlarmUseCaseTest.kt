package id.ac.ugm.sv.trpl.glucosemonitoring.usecase

import android.annotation.SuppressLint
import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseAlarmLevel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.GlucoseAlarm
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.ShowGlucoseAlarmUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class ShowGlucoseAlarmUseCaseTest {
    
    private lateinit var testScheduler: TestScheduler
    
    private lateinit var showGlucoseAlarmUseCase: ShowGlucoseAlarmUseCase
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    
    private val fetchDelayMinutes = 1L
    
    @Before
    fun setUp() {
        testScheduler = TestScheduler()
        RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
        
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        
        showGlucoseAlarmUseCase = ShowGlucoseAlarmUseCase(
            glucoseRepository = fakeGlucoseRepository,
            userRepository = FakeUserRepository(),
        )
    }
    
    @SuppressLint("CheckResult")
    @Test
    fun `Fetch recent glucose data, verify fetch data every 5 minute`() {
        val fetchCount = 4
        
        showGlucoseAlarmUseCase().test()
        testScheduler.advanceTimeBy(fetchDelayMinutes * fetchCount, TimeUnit.MINUTES)
        
        verify(exactly = fetchCount) {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given hyperglycemia level 2 data, return hyperglycemia level 2 alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose7))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.DANGEROUSLY_HIGH,
            expectedGlucoseLevel = GlucoseCategory.HYPERGLYCEMIA_LEVEL_2.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = true,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given hyperglycemia level 1 data, return hyperglycemia level 1 alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose6))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.HIGH,
            expectedGlucoseLevel = GlucoseCategory.HYPERGLYCEMIA_LEVEL_1.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = true,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given in range towards high glucose, return towards high alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose5))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.TOWARDS_HIGH,
            expectedGlucoseLevel = GlucoseCategory.IN_RANGE.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = false,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given in range glucose, return normal alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose4))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.NORMAL,
            expectedGlucoseLevel = GlucoseCategory.IN_RANGE.range,
            expectedAlertWithSound = false,
            expectedAlertWithVibration = false,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given in range towards low glucose, return towards low alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose3))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.TOWARDS_LOW,
            expectedGlucoseLevel = GlucoseCategory.IN_RANGE.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = false,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given hypoglycemia level 1 data, return hypoglycemia level 1 alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose2))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.LOW,
            expectedGlucoseLevel = GlucoseCategory.HYPOGLYCEMIA_LEVEL_1.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = true,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Fetch recent glucose data, given hypoglycemia level 2 data, return hypoglycemia level 2 alert`() {
        every { fakeGlucoseRepository.monitorGlucoseData(any()) } returns Flowable.just(
            Result.Success(listOf(DummyData.glucose1))
        )
        
        val observable = showGlucoseAlarmUseCase()
        val testObservable = observable.test()
        testScheduler.advanceTimeBy(fetchDelayMinutes, TimeUnit.MINUTES)
        val glucoseAlert = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.monitorGlucoseData(any())
        }
        
        assertGlucoseAlert(
            expectedAlertLevel = GlucoseAlarmLevel.DANGEROUSLY_LOW,
            expectedGlucoseLevel = GlucoseCategory.HYPOGLYCEMIA_LEVEL_2.range,
            expectedAlertWithSound = true,
            expectedAlertWithVibration = true,
            actualGlucoseAlarm = glucoseAlert,
        )
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    private fun assertGlucoseAlert(
        expectedAlertLevel: GlucoseAlarmLevel,
        expectedGlucoseLevel: IntRange,
        expectedAlertWithSound: Boolean,
        expectedAlertWithVibration: Boolean,
        actualGlucoseAlarm: GlucoseAlarm,
    ) {
        assertThat(actualGlucoseAlarm.alarmLevel).isEqualTo(expectedAlertLevel)
        assertThat(actualGlucoseAlarm.glucoseLevel).isIn(expectedGlucoseLevel)
        assertThat(actualGlucoseAlarm.alertWithSound).isEqualTo(expectedAlertWithSound)
        assertThat(actualGlucoseAlarm.alertWithVibration).isEqualTo(expectedAlertWithVibration)
    }
    
    @After
    fun tearDown() {
        testScheduler.shutdown()
        RxJavaPlugins.reset()
        unmockkAll()
    }
    
}