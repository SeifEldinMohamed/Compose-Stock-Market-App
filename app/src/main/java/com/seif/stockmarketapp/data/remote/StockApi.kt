package com.seif.stockmarketapp.data.remote

import com.seif.stockmarketapp.BuildConfig
import com.seif.stockmarketapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ):ResponseBody // to get access to a file stream and to download that file (access to byte stream).

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv") // we get the close value every hour
    suspend fun getIntraDayInfo(
        @Query("symbol") symbol:String,
        @Query("apikey") apiKey:String = BuildConfig.API_KEY,
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol:String,
        @Query("apikey") apiKey:String = BuildConfig.API_KEY,
    ): CompanyInfoDto

}
// we will use retrofit to download that csv file and then take the
// byte stream(stream from api) and parse it with opencsv.
