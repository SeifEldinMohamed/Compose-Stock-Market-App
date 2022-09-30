package com.seif.stockmarketapp.data.repository

import com.seif.stockmarketapp.data.local.LocalDataSource
import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity
import com.seif.stockmarketapp.data.mapper.toCompanyListing
import com.seif.stockmarketapp.data.remote.RemoteDataSource
import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.domain.repository.StockRepository
import com.seif.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // to make sure that we have a single StockRepository in whole app
class StockRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
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
            val remoteListing = try {
                val response = remoteDataSource.getListing()
                response.byteStream() // used to read csv file
            } catch (e: IOException) { //ex: something with thw parsing goes wrong
                emit(Resource.Error("couldn't load data"))
            } catch (e: HttpException) { // ex: invalid response
                emit(Resource.Error("couldn't load data"))
            }
        }
    }
}