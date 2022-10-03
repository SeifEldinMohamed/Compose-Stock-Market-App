package com.seif.stockmarketapp.presentation.company_info
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.stockmarketapp.domain.usecase.GetCompanyInfoUseCase
import com.seif.stockmarketapp.domain.usecase.GetIntraDayInfoUseCase
import com.seif.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, // way to get access to navigation arguments in the viewModel directory without passing them from the ui
    private val getCompanyInfoUseCase: GetCompanyInfoUseCase,
    private val getIntraDayInfoUseCase: GetIntraDayInfoUseCase

): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
       getCompanyInfo()
    }

    private fun getCompanyInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            val companyInfoResult = async { getCompanyInfoUseCase(symbol) } // we use async to do the 2 network calls asyncronosly
            val intraDayInfoResult = async { getIntraDayInfoUseCase(symbol) }

            when(val result = companyInfoResult.await()) { // get value from async block
                is Resource.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> Unit
            }
            /** IntraDay Info **/
            when(val result = intraDayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> Unit
            }
        }
    }
}