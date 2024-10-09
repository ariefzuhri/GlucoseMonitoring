package id.ac.ugm.sv.trpl.glucosemonitoring.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.ugm.sv.trpl.glucosemonitoring.BuildConfig
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.network.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Singleton
    @Provides
    fun provideChuckerInterceptor(application: Application): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(application)
            .build()
    }
    
    @Singleton
    @Provides
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .build()
    }
    
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.apiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
    
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    
}