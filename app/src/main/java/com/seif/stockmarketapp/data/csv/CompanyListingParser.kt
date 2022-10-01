package com.seif.stockmarketapp.data.csv

import com.opencsv.CSVReader
import com.seif.stockmarketapp.domain.model.CompanyListing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor() : CSVParser<CompanyListing> {
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll() // will return a list of array
                .drop(1) // drop the first field ( first row )
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListing(
                        name = name?: return@mapNotNull null,
                        symbol = symbol?: return@mapNotNull null,
                        exchange = exchange?: return@mapNotNull null
                    )
                }.also {
                    csvReader.close()
                }
        }
    }
}