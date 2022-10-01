package com.seif.stockmarketapp.presentation.companies_list

sealed class CompanyListingEvent { // for different ui events we can perform on a single screen
    object Refresh : CompanyListingEvent()
    data class OnSearchQueryChange(val query: String) : CompanyListingEvent()
    // clicking on an Item will not be an event, since we can directly navigate to the new screen
}
