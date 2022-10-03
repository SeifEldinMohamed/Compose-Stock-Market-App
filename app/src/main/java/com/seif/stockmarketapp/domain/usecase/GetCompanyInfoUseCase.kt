package com.seif.stockmarketapp.domain.usecase

import com.seif.stockmarketapp.domain.model.CompanyInfo
import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.util.Resource
import javax.inject.Inject

class GetCompanyInfoUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(symbol: String): Resource<CompanyInfo> {
        return stockRepository.getCompanyInfo(symbol)
    }
}