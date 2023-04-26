package com.example.smartpowerconnector_room.di
import android.content.Context
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.internet.idata.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BroadcastModule {


    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideMyAwsApi(): AwsApiService {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com")
            .build()
            .create(AwsApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesMyAwsRepository(aws: AwsApiService): AwsRepository {
        return NetworkDeviceRepository(aws)
    }
}

