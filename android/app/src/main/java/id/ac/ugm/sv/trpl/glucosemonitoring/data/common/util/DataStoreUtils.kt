package id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> RxDataStore<Preferences>.get(
    key: Preferences.Key<T>,
    defaultValue: T,
): Flowable<T> {
    return data().map { preferences ->
        preferences[key] ?: defaultValue
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> RxDataStore<Preferences>.save(
    key: Preferences.Key<T>,
    value: T,
): Completable {
    return updateDataAsync { preferences ->
        val mutablePreferences = preferences.toMutablePreferences()
        mutablePreferences[key] = value
        Single.just(mutablePreferences)
    }.ignoreElement()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun RxDataStore<Preferences>.clearData(): Completable {
    return updateDataAsync { preferences ->
        val mutablePreferences = preferences.toMutablePreferences()
        mutablePreferences.clear()
        Single.just(mutablePreferences)
    }.ignoreElement()
}