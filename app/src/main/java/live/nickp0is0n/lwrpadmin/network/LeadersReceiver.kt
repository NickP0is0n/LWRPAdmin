/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import live.nickp0is0n.lwrpadmin.models.Leader
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import org.json.JSONArray

class LeadersReceiver : DataReceiver {
    val notifier = QueryNotifier()
    private var data: JSONArray? = null

    override fun receiveData(data: Any) {
        this.data = data as JSONArray
        notifier.resultType = QueryType.LEADER_LIST
        notifier.status = QueryStatus.SUCCESS
        notifier.notifyObserver()
    }

    override fun errorCallback(text: String) {
        notifier.resultType = QueryType.LEADER_LIST
        notifier.status = QueryStatus.ERROR
        notifier.notifyObserver()
    }

    override fun getData(): ArrayList<Leader> {
        val leaders = ArrayList<Leader>()
        for (i in 0 until data!!.length()) {
            leaders.add(Leader(
                nickname = data!!.getJSONArray(i).getString(0),
                fractionId = data!!.getJSONArray(i).getInt(1),
                getOnDate = data!!.getJSONArray(i).getString(2),
                isOnline = data!!.getJSONArray(i).getInt(3) != 0,
                playersInFractionOnline = data!!.getJSONArray(i).getInt(4),
                level = data!!.getJSONArray(i).getInt(5),
                experience = data!!.getJSONArray(i).getInt(6)
            ))
        }
        return leaders
    }
}