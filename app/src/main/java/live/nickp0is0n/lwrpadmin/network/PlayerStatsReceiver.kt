/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.PlayerStats
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class PlayerStatsReceiver : DataReceiver {
    val notifier = QueryNotifier()
    private var data: JSONObject? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONObject
        notifier.resultType = QueryType.PLAYER_STATS
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.PLAYER_STATS
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): PlayerStats? =
        if (data == null || data!!.getString("nickname") == "null") null
        else PlayerStats(
            data!!.getString("nickname"),
            data!!.getInt("level"),
            data!!.getInt("lawLevel"), // законопослушность
            data!!.getInt("warns"),
            data!!.getInt("drugAddiction"),
            data!!.getInt("fraction"),
            data!!.getInt("rank"),
            data!!.getInt("house"),
            data!!.getString("cars"),
            data!!.getInt("business")
        )
}