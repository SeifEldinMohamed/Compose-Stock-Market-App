package com.seif.stockmarketapp.data.repository

import com.seif.stockmarketapp.R
import com.seif.stockmarketapp.data.csv.CSVParser
import com.seif.stockmarketapp.data.local.LocalDataSource
import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity
import com.seif.stockmarketapp.data.mapper.toCompanyListing
import com.seif.stockmarketapp.data.mapper.toCompanyListingEntity
import com.seif.stockmarketapp.data.remote.RemoteDataSource
import com.seif.stockmarketapp.domain.model.CompanyListing
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
    private val companyListingParser: CSVParser<CompanyListing>
) : StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> { // TODO: use networkBoundResource
        return flow {
            emit(Resource.Loading(true))
            val localListing: List<CompanyListingEntity> =
                localDataSource.searchCompanyListings(query)
            emit(Resource.Success(data = localListing.map { it.toCompanyListing() }))

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
}