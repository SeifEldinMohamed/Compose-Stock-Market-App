package com.seif.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(
        @Query("apikey") apiKey: String
    ):ResponseBody // to get access to a file stream and to download that file (access to byte stream).
}
// we will use retrofit to download that csv file and then take the
// byte stream(stream from api) and parse it with opencsv.
