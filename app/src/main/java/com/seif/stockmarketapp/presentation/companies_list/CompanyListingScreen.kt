package com.seif.stockmarketapp.presentation.companies_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.seif.stockmarketapp.R
import com.seif.stockmarketapp.presentation.companies_list.component.CompanyItem
import com.seif.stockmarketapp.presentation.destinations.CompanyInfoScreenDestination
import com.seif.stockmarketapp.util.UiText

@Destination(start = true)
@Composable
fun CompanyListingScreen(
    navigator: DestinationsNavigator, // comes from decomposed destination library
    viewModel: CompanyListingViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit){
        viewModel.getCompanyListing()
    }
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text(text = UiText.StringResource(R.string.search_placholder).asString())
            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(state = swipeRefreshState, onRefresh = {
            viewModel.onEvent(CompanyListingEvent.Refresh)
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companies.size) { i ->
                    if (i < state.companies.size) // to prevent last item from having the divider
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    CompanyItem(
                        company = state.companies[i],
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(symbol = state.companies[i].symbol)
                                )
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}