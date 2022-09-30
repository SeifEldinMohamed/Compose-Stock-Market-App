package com.seif.stockmarketapp.data.local

import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dao: StockDao
) {
    suspend fun getCompanyListings(): List<CompanyListingEntity> {
        return dao.getCompanyListings()
    }

    suspend fun insertCompanyListings(companyListingEntities: List<CompanyListingEntity>) {
        dao.insertCompanyListings(companyListingEntities)
    }

    suspend fun clearCompanyListings() {
        dao.clearCompanyListings()
    }

    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity> {
        return dao.searchCompanyListings(query)
    }
}