package com.seif.stockmarketapp.domain.repository

import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote:Boolean,
        query:String
    ): Flow<Resource<List<CompanyListing>>>
}