/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.service

import live.nickp0is0n.lwrpadmin.network.QueryType

interface Observer {
    fun update(status: QueryStatus, resultType: QueryType)
}