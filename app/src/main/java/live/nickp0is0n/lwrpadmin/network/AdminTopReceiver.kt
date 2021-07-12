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

class AdminTopReceiver : AdminListReceiver() {
    override var data: JSONArray? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONArray
        notifier.resultType = QueryType.ADMIN_TOP
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.ADMIN_TOP
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }
}