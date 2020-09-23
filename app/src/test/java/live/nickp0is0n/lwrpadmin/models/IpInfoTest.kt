/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.models

import org.junit.Assert.*
import org.junit.Test

class IpInfoTest {
    @Test
    fun getIpInfo_retrieveSampleDataCenterIpInfo_sameInfoReturns() {
        val sampleIpInfo = listOf("91.109.200.200", "AS199669 Okay-Telecom Ltd.", "RU", "Novyy Urengoy")
        val ipInfoRetriever = IpInfo("91.109.200.200")
        val onlineRetrievedIpInfo = listOf(ipInfoRetriever.ip, ipInfoRetriever.provider, ipInfoRetriever.country, ipInfoRetriever.city)
        assertEquals(sampleIpInfo, onlineRetrievedIpInfo)
    }
}