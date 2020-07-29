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
import org.json.JSONArray

class AdminListReceiver : DataReceiver {
    val notifier = QueryNotifier()
    private var data: JSONArray? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONArray
        notifier.queueName = "adminList"
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.queueName = "adminList"
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): ArrayList<Admin> {
        val admins = ArrayList<Admin>()
        for (i in 0 until data!!.length()) {
            admins.add(Admin(
                nickname = data!!.getJSONArray(i).getString(0),
                adminLevel = data!!.getJSONArray(i).getInt(1),
                mondayOnline = data!!.getJSONArray(i).getInt(2),
                tuesdayOnline = data!!.getJSONArray(i).getInt(3),
                wednesdayOnline = data!!.getJSONArray(i).getInt(4),
                thursdayOnline = data!!.getJSONArray(i).getInt(5),
                fridayOnline = data!!.getJSONArray(i).getInt(6),
                saturdayOnline = data!!.getJSONArray(i).getInt(7),
                sundayOnline = data!!.getJSONArray(i).getInt(8),
                reportsAnswered = data!!.getJSONArray(i).getInt(9),
                muted = data!!.getJSONArray(i).getInt(10),
                jailed = data!!.getJSONArray(i).getInt(11),
                pAvig = data!!.getJSONArray(i).getInt(12)
            ))
        }
        return admins
    }
}