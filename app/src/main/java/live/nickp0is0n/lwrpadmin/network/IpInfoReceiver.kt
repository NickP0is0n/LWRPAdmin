/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.IpInfo
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class IpInfoReceiver : DataReceiver {
    val notifier = QueryNotifier()
    private var data: JSONObject? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONObject
        notifier.resultType = QueryType.IP_INFO
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.IP_INFO
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): List<IpInfo>? =
        if (data!!.getString("pGetonIP") == "null") null
        else listOf(IpInfo(data!!.getString("pIpReg")),
            IpInfo((data!!.getString("pGetonIP"))))

}