/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import live.nickp0is0n.lwrpadmin.models.IpInfo
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class IpInfoReceiver : DataReceiver, ViewModel() {
    val notifier = QueryNotifier()
    private var data: JSONObject? = null
    private var ipList: List<IpInfo>? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONObject
        notifier.resultType = QueryType.IP_INFO
        notifier.status = QueryStatus.SUCCESS
        val job = viewModelScope.launch(Dispatchers.IO) {
            if (data.getString("pGetonIP") != "null") {
                ipList = listOf(IpInfo(data.getString("pIpReg")),
                    IpInfo((data.getString("pGetonIP"))))
                ipList?.forEach {
                    val response = it.infoProvider.lookupIP(it.ip)
                    it.provider = response.org
                    it.country = response.countryCode
                    it.city = response.city
                }
                notifier.notifyObserver()
            }
        }
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.IP_INFO
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): List<IpInfo>? = ipList

}