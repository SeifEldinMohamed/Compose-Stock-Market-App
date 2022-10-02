package com.seif.stockmarketapp.domain.usecase

import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCompanyListingUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(shouldFetch: Boolean = false, query: String):
            Flow<Resource<List<CompanyListing>>> {
        return stockRepository.getCompanyListings(shouldFetch, query)
    }
}