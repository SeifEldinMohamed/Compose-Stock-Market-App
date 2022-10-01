package com.seif.stockmarketapp.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = emptyArray() // if it's an array
    ) : UiText()

    @Composable
    fun asString(): String { // extract this actual string out of this ui text
        return when(this) { // this: instance of UiTest
            is DynamicString -> value
            is StringResource -> stringResource(id, args)
        }
    }
}