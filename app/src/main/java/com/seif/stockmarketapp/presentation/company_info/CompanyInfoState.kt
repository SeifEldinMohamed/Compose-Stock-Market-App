package com.seif.stockmarketapp.presentation.company_info

import com.seif.stockmarketapp.domain.model.CompanyInfo
import com.seif.stockmarketapp.domain.model.IntraDayInfo
import com.seif.stockmarketapp.util.UiText

data class CompanyInfoState(
    val stockInfos: List<IntraDayInfo> = emptyList(), // used in chart later on
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)