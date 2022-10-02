package com.seif.stockmarketapp.di

import android.content.Context
import androidx.room.Room
import com.seif.stockmarketapp.data.local.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStockDatabase(
        @ApplicationContext context: Context
    ): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stock_db"
        ).build()
    }
    @Singleton
    @Provides
    fun provideDao(database: StockDatabase) = database.stockDao
}