/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONObject

class UserCredentialsReceiver : DataReceiver {
    val notifier = QueryNotifier()
    var data: JSONObject? = null

    override fun receiveData(data: JSONObject) {
        this.data = data
        notifier.queueName = "credentials"
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.queueName = "credentials"
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): Any? = if (data!!.isNull("nickname")) null
        else User(data!!.getString("nickname"), data!!.getString("password"))
}