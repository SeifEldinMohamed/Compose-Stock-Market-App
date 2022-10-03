package com.seif.stockmarketapp.domain.model

data class CompanyInfo(
    val symbol: String?,
    val assetType: String?,
    val description: String?,
    val name: String?,
    val country: String?,
    val industry: String?,
    val sector: String?
)