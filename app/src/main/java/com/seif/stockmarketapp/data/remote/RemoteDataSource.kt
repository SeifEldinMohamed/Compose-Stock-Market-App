package com.seif.stockmarketapp.data.remote

import com.seif.stockmarketapp.BuildConfig
import com.seif.stockmarketapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val stockApi: StockApi
) {
    suspend fun getListing(apiKey: String = BuildConfig.API_KEY): ResponseBody {
        return stockApi.getListing(apiKey)
    }

    suspend fun getIntraDayInfo(symbol:String): ResponseBody {
        return stockApi.getIntraDayInfo(symbol)
    }
    suspend fun getCompanyInfo(symbol:String): CompanyInfoDto {
        return stockApi.getCompanyInfo(symbol)
    }
}