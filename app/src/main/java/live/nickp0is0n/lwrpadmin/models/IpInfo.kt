/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ipinfo.api.IPInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

data class IpInfo (val ip: String): ViewModel() {
    lateinit var provider: String
    lateinit var country: String
    lateinit var city: String

    private val TOKEN = "7aa3f036a9508e"

    val infoProvider: IPInfo = IPInfo.builder().setToken(TOKEN).build()
}