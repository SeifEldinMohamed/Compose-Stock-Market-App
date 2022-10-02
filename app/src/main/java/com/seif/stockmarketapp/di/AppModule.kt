package com.seif.stockmarketapp.di

import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.domain.usecase.GetCompanyListingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGetCompanyListingUseCase(
        stockRepository: StockRepository
    ): GetCompanyListingUseCase {
        return GetCompanyListingUseCase(stockRepository)
    }
}