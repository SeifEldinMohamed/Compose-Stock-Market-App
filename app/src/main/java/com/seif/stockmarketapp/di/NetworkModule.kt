package com.seif.stockmarketapp.di

import com.seif.stockmarketapp.BuildConfig
import com.seif.stockmarketapp.data.remote.RemoteDataSource
import com.seif.stockmarketapp.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        stockApi: StockApi
    ): RemoteDataSource {
        return RemoteDataSource(stockApi)
    }
}