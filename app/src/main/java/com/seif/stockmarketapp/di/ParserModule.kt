package com.seif.stockmarketapp.di

import com.seif.stockmarketapp.data.csv.CSVParser
import com.seif.stockmarketapp.data.csv.CompanyListingParser
import com.seif.stockmarketapp.domain.model.CompanyListing
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParserModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>
}