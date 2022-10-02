package com.seif.stockmarketapp.presentation.companies_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.stockmarketapp.R
import com.seif.stockmarketapp.domain.usecase.GetCompanyListingUseCase
import com.seif.stockmarketapp.util.Resource
import com.seif.stockmarketapp.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val getCompanyListingUseCase: GetCompanyListingUseCase
) : ViewModel() {

    var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null // we will keep track of our coroutine job and whenever we typing something new we add a little bit of delay until we actually start the search

    fun onEvent(event: CompanyListingEvent) {
        when (event) {
            is CompanyListingEvent.OnSearchQueryChange -> { // will be trigger for each character we type
                // because we automatically search when we tap on something the solution is to use searchJob
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch(Dispatchers.IO) {
                    delay(500L)
                    getCompanyListing()
                }

            }
            is CompanyListingEvent.Refresh -> {
                getCompanyListing(shouldFetchFromRemote = true)
            }
        }

    }

    fun getCompanyListing(
        shouldFetchFromRemote: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getCompanyListingUseCase(shouldFetchFromRemote, query)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            state = state.copy(
                                error = result.message
                            )
                            Log.d("TAG", "getCompanyListing: Error ${result.message}")
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                            Log.d("TAG", "getCompanyListing: Loading....")

                        }
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(companies = listings)
                                Log.d("TAG", "getCompanyListing: success ${listings}")
                            }
                        }
                    }
                }
        }
    }
}