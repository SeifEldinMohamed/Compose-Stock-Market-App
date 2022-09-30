package com.seif.stockmarketapp.data.remote

import com.seif.stockmarketapp.BuildConfig
import okhttp3.ResponseBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val stockApi: StockApi
) {
    suspend fun getListing(apiKey: String = BuildConfig.API_KEY): ResponseBody {
        return stockApi.getListing(apiKey)
    }
}