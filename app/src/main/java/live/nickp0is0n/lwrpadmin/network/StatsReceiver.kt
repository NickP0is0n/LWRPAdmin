/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class StatsReceiver : DataReceiver {
    val notifier = QueryNotifier()
    var data: JSONObject? = null

    override fun receiveData(data: JSONObject) {
        this.data = data
        notifier.queueName = "adminInfo"
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.queueName = "adminInfo"
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): Admin? =
        if (data!!.getString("nickname") == "null") null
        else Admin(
            data!!.getString("nickname"),
            data!!.getInt("adminLevel"),
            data!!.getInt("mondayOnline"),
            data!!.getInt("tuesdayOnline"),
            data!!.getInt("wednesdayOnline"),
            data!!.getInt("thursdayOnline"),
            data!!.getInt("fridayOnline"),
            data!!.getInt("saturdayOnline"),
            data!!.getInt("sundayOnline"),
            data!!.getInt("reportsAnswered"),
            data!!.getInt("muted"),
            data!!.getInt("jailed"),
            data!!.getInt("pAvig")
        )
}