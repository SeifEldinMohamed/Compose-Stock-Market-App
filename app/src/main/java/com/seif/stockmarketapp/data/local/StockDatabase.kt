package com.seif.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity

@Database(entities = [CompanyListingEntity::class], version = 1, exportSchema = true)
abstract class StockDatabase: RoomDatabase() {
    abstract val stockDao: StockDao
}