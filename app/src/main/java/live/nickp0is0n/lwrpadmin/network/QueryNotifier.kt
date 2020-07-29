/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.service.Notifier
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus

class QueryNotifier : Notifier {
    val observerList = ArrayList<Observer>()

    var status = QueryStatus.ERROR
    var queueName = ""

    override fun addObserver(observer: Observer) {
        observerList.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        if(!observerList.contains(observer)) throw Exception("Observer does not registered in notifier.")
        observerList.remove(observer)
    }

    override fun notifyObserver() {
        observerList.forEach {
            it.update(this.status, this.queueName)
        }
    }
}