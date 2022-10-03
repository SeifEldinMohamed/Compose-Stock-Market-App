package com.seif.stockmarketapp.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.seif.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.seif.stockmarketapp.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {
    val pattern = "yyyy-MM-dd:HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(this.timestamp, formatter)
    return IntraDayInfo(
        date = localDateTime,
        close = this.close
    )
}