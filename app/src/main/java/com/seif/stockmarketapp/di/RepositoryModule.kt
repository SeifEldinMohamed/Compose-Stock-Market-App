package com.seif.stockmarketapp.di

import com.seif.stockmarketapp.data.csv.CSVParser
import com.seif.stockmarketapp.data.csv.IntraDayInfoParser
import com.seif.stockmarketapp.data.local.LocalDataSource
import com.seif.stockmarketapp.data.remote.RemoteDataSource
import com.seif.stockmarketapp.data.repository.StockRepositoryImp
import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.domain.model.IntraDayInfo
import com.seif.stockmarketapp.domain.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        csvParser: CSVParser<CompanyListing>,
        intraDayInfoParser: CSVParser<IntraDayInfo>
    ): StockRepository {
        return StockRepositoryImp(localDataSource, remoteDataSource, csvParser, intraDayInfoParser)
    }
}