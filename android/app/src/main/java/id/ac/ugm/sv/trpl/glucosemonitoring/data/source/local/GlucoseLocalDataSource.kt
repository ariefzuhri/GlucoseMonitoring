package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.GlucoseEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.GlucoseDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlucoseLocalDataSource @Inject constructor(
    private val glucoseDao: GlucoseDao,
) {
    
    fun getGlucoses(): Flowable<List<GlucoseEntity>> {
        return glucoseDao.getGlucoses()
    }
    
    fun insertGlucoses(glucoseEntities: List<GlucoseEntity>): Completable {
        return glucoseDao.insertGlucoses(glucoseEntities)
    }
    
    fun deleteGlucoses(): Completable {
        return glucoseDao.deleteGlucoses()
    }
    
}