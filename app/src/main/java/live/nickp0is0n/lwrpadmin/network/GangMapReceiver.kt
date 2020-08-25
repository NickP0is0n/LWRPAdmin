/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.GangMap
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class GangMapReceiver : DataReceiver {
    val notifier = QueryNotifier()
    private var data: JSONObject? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONObject
        notifier.resultType = QueryType.GANG_MAP
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.GANG_MAP
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): GangMap? =
        if (data!!.getString("groveTerritories") == "null") null
        else GangMap(
            data!!.getInt("groveTerritories"),
            data!!.getInt("groveOnline"),
            data!!.getInt("ballasTerritories"),
            data!!.getInt("ballasOnline"),
            data!!.getInt("vagosTerritories"),
            data!!.getInt("vagosOnline"),
            data!!.getInt("rifaTerritories"),
            data!!.getInt("rifaOnline"),
            data!!.getInt("aztecTerritories"),
            data!!.getInt("aztecOnline"),
            data!!.getString("lastUpdate")
        )
}