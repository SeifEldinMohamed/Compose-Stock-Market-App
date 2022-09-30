package com.seif.stockmarketapp.data.local

import androidx.room.*
import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity

@Dao
interface StockDao {

    @Query("SELECT * FROM company_listing_entity")
    suspend fun getCompanyListings(): List<CompanyListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(companyListingEntities: List<CompanyListingEntity>)

    @Query("DELETE FROM company_listing_entity")
    suspend fun clearCompanyListings()

    @Query(
        """
        SELECT * 
        FROM company_listing_entity 
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
        UPPER(:query) == symbol
    """
    )
    suspend fun searchCompanyListings(query:String): List<CompanyListingEntity>
}