package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.Constants
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseAlarmLevel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.GlucoseAlarm
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class ShowGlucoseAlarmUseCase @Inject constructor(
    private val glucoseRepository: IGlucoseRepository,
    private val userRepository: IUserRepository,
) {
    
    operator fun invoke(): Flowable<GlucoseAlarm> {
        return userRepository.getProfile()
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .take(1)
            .flatMap { profile ->
                Flowable.interval(
                    Constants.FETCH_INTERVAL, // 5 minutes
                    TimeUnit.SECONDS,
                    Schedulers.newThread(),
                ).flatMap {
                    glucoseRepository.monitorGlucoseData(profile.patientId)
                        .filter { result ->
                            result is Result.Success
                        }
                        .map { result ->
                            val recentGlucoseData =
                                (result as Result.Success<List<Glucose>>).data!!
                            // recentGlucoseData.maxBy { it.dateTime }
                            recentGlucoseData.random() // For simulation
                        }
                        .map { recentGlucoseData ->
                            val glucoseLevel = recentGlucoseData.level.roundToInt()
                            val alarmLevel = GlucoseAlarmLevel.fromInt(glucoseLevel)
                            val alertWithSound = alarmLevel != GlucoseAlarmLevel.NORMAL
                            val alertWithVibration =
                                alarmLevel != GlucoseAlarmLevel.NORMAL &&
                                        alarmLevel != GlucoseAlarmLevel.TOWARDS_LOW &&
                                        alarmLevel != GlucoseAlarmLevel.TOWARDS_HIGH
                            GlucoseAlarm(
                                alarmLevel = alarmLevel,
                                glucoseLevel = glucoseLevel,
                                alertWithSound = alertWithSound,
                                alertWithVibration = alertWithVibration,
                            )
                        }
                }
            }
    }
    
}