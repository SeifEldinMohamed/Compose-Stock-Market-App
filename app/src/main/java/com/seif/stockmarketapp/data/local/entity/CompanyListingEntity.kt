package com.seif.stockmarketapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_listing_entity")
data class CompanyListingEntity(
    @PrimaryKey
    val id:Int? = null,
    val name: String,
    val symbol: String,
    val exchange: String
)
