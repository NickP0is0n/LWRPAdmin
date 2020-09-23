/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.models

import io.ipinfo.api.IPInfo

data class IpInfo (val ip: String) {
    val provider: String
    val country: String
    val city: String

    val ACCESS_TOKEN = "7aa3f036a9508e"

    init {
        val ipInfoProvider = IPInfo.builder().setToken(ACCESS_TOKEN).build()
        val ipInfoResponse = ipInfoProvider.lookupIP(ip)
        provider = ipInfoResponse.org
        country = ipInfoResponse.countryCode
        city = ipInfoResponse.city
    }
}