package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.UserMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.UserLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.UserRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostLoginResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var userRepository: UserRepository
    private lateinit var userLocalDataSource: UserLocalDataSource
    private lateinit var userRemoteDataSource: UserRemoteDataSource
    private lateinit var userMapper: UserMapper
    
    @Before
    fun setUp() {
        userLocalDataSource = mockk()
        userRemoteDataSource = mockk()
        userMapper = mockk()
        
        userRepository = UserRepository(
            userLocalDataSource = userLocalDataSource,
            userRemoteDataSource = userRemoteDataSource,
            userMapper = userMapper,
        )
    }
    
    @Test
    fun `Login, remote service works as expected, return success with account data`() {
        val dummyEmail = "johndoe@mail.com"
        val dummyPassword = "123456"
        val dummySuccessResponse = PostLoginResponse(
            data = PostLoginResponse.Data(
                id = 1,
                ptId = 1,
                email = dummyEmail,
                fullname = "John Doe",
                name = "John Doe",
                photo = null,
                photos = null,
                address = null,
                company = null,
                status = null,
                isAdmin = null,
                createdOn = null,
            )
        )
        val dummyDomain = User(
            patientId = 1,
            name = "John Doe",
            email = dummyEmail,
        )
        
        every { userRemoteDataSource.postLogin(dummyEmail, dummyPassword) } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { userMapper.mapToDomain(dummySuccessResponse) } returns
                dummyDomain
        
        val observable = userRepository.login(dummyEmail, dummyPassword)
        val testObservable = observable.test()
        
        verifySequence {
            userRemoteDataSource.postLogin(eq(dummyEmail), eq(dummyPassword))
            userMapper.mapToDomain(eq(dummySuccessResponse))
            userLocalDataSource wasNot Called
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Success(dummyDomain)
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            userRemoteDataSource,
            userMapper,
            userLocalDataSource,
        )
    }
    
    @Test
    fun `Login, remote service doesn't work as expected, return failed`() {
        val dummyEmail = "johndoe@mail.com"
        val dummyPassword = "123456"
        val dummyFailedResponse = ""
        
        every { userRemoteDataSource.postLogin(dummyEmail, dummyPassword) } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = userRepository.login(dummyEmail, dummyPassword)
        val testObservable = observable.test()
        
        verifySequence {
            userRemoteDataSource.postLogin(eq(dummyEmail), eq(dummyPassword))
            userMapper wasNot Called
            userLocalDataSource wasNot Called
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Failed
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            userRemoteDataSource,
            userMapper,
            userLocalDataSource,
        )
    }
    
    @Test
    fun `Get profile, return profile`() {
        val dummyPatientId = 1
        val dummyName = "John Doe"
        val dummyEmail = "johndoe@mail.com"
        
        every { userLocalDataSource.getPatientId() } returns Flowable.just(
            dummyPatientId
        )
        every { userLocalDataSource.getName() } returns Flowable.just(
            dummyName
        )
        every { userLocalDataSource.getEmail() } returns Flowable.just(
            dummyEmail
        )
        
        val observable = userRepository.getProfile()
        val testObservable = observable.test()
        
        verify {
            userLocalDataSource.getPatientId()
            userLocalDataSource.getName()
            userLocalDataSource.getEmail()
        }
        
        val expectedProfile = User(
            patientId = dummyPatientId,
            name = dummyName,
            email = dummyEmail,
        )
        testObservable.assertResult(expectedProfile)
        
        confirmVerified(userLocalDataSource)
    }
    
    @Test
    fun `Save profile, return complete`() {
        val dummyPatientId = 1
        val dummyName = "John Doe"
        val dummyEmail = "johndoe@mail.com"
        
        every { userLocalDataSource.savePatientId(any()) } returns
                Completable.complete()
        every { userLocalDataSource.saveName(any()) } returns
                Completable.complete()
        every { userLocalDataSource.saveEmail(any()) } returns
                Completable.complete()
        
        val observable = userRepository.saveProfile(
            patientId = dummyPatientId,
            name = dummyName,
            email = dummyEmail,
        )
        val testObservable = observable.test()
        
        verify {
            userLocalDataSource.savePatientId(eq(dummyPatientId))
            userLocalDataSource.saveName(eq(dummyName))
            userLocalDataSource.saveEmail(eq(dummyEmail))
        }
        
        testObservable.assertComplete()
        
        confirmVerified(userLocalDataSource)
    }
    
    @Test
    fun `Clear user data, return complete`() {
        every { userLocalDataSource.clearData() } returns
                Completable.complete()
        
        val observable = userRepository.clearUserData()
        val testObservable = observable.test()
        
        verify {
            userLocalDataSource.clearData()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(userLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}