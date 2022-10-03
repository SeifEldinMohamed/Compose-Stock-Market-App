package com.seif.stockmarketapp.data.mapper

import com.seif.stockmarketapp.data.local.entity.CompanyListingEntity
import com.seif.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.seif.stockmarketapp.domain.model.CompanyInfo
import com.seif.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        assetType = assetType ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: "",
        sector = sector ?: ""
    )// the fields may be null if we finish our free quota
}