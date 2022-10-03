package com.seif.stockmarketapp.domain.usecase

import com.seif.stockmarketapp.domain.model.IntraDayInfo
import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.util.Resource
import javax.inject.Inject

class GetIntraDayInfoUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(symbol: String): Resource<List<IntraDayInfo>> {
        return stockRepository.getIntraDayInfo(symbol)
    }
}