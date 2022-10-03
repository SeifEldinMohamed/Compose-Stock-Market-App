package com.seif.stockmarketapp.data.repository

import android.util.Log
import com.seif.stockmarketapp.R
import com.seif.stockmarketapp.data.csv.CSVParser
import com.seif.stockmarketapp.data.csv.IntraDayInfoParser
import com.seif.stockmarketapp.data.local.LocalDataSource
import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity
import com.seif.stockmarketapp.data.mapper.toCompanyInfo
import com.seif.stockmarketapp.data.mapper.toCompanyListing
import com.seif.stockmarketapp.data.mapper.toCompanyListingEntity
import com.seif.stockmarketapp.data.remote.RemoteDataSource
import com.seif.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.seif.stockmarketapp.domain.model.CompanyInfo
import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.domain.model.IntraDayInfo
import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.util.Resource
import com.seif.stockmarketapp.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // to make sure that we have a single StockRepository in whole app
class StockRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val interDayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> { // TODO: use networkBoundResource
        return flow {
            emit(Resource.Loading(true))
            Log.d("TAG2", "getCompanyListings: Loading...")
            val localListing: List<CompanyListingEntity> =
                localDataSource.searchCompanyListings(query)
            emit(Resource.Success(data = localListing.map { it.toCompanyListing() }))
            Log.d("TAG2", "getCompanyListings: Success ${localListing.map { it.toCompanyListing()}}")

            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing: List<CompanyListing>? = try {
                val response = remoteDataSource.getListing()
                companyListingParser.parse(response.byteStream()) // byteStream: // used to read csv file
            } catch (e: IOException) { //ex: something with thw parsing goes wrong
                e.printStackTrace()
                emit(Resource.Error(UiText.StringResource(R.string.io_exception_error_message)))
                null
            } catch (e: HttpException) { // ex: invalid response
                e.printStackTrace()
                emit(Resource.Error(UiText.StringResource(R.string.http_exception_error_message)))
                null
            }
            remoteListing?.let { listings ->
                localDataSource.clearCompanyListings()
                localDataSource.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = localDataSource.getCompanyListings().map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = remoteDataSource.getIntraDayInfo(symbol)
            val results = interDayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                UiText.StringResource(R.string.intraday_info_io_error)
            )
        } catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                UiText.StringResource(R.string.intraday_info_http_error)
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result: CompanyInfoDto = remoteDataSource.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                UiText.StringResource(R.string.company_info_io_error)
            )
        } catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                UiText.StringResource(R.string.company_info_http_error)
            )
        }
    }
}