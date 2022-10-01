package com.seif.stockmarketapp.presentation.companies_list

import com.seif.stockmarketapp.domain.model.CompanyListing
import com.seif.stockmarketapp.util.UiText

data class CompanyListingState( // contains everything relevant to ui state
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing:Boolean = false,
    val searchQuery:String = "",
    val error: UiText? = null
)
