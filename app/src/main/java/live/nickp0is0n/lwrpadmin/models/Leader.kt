/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Базовая информация о лидере организации и
 * текущее количество игроков внутри нее.
 * @property nickname
 * @property fractionId
 * @property getOnDate
 * @property isOnline
 * @property playersInFractionOnline
 * @property level
 * @property experience
 */
@Parcelize
data class Leader(
    val nickname: String,
    val fractionId: Int,
    /** Дата последнего захода лидера */
    val getOnDate: String,
    val isOnline: Boolean,
    val playersInFractionOnline: Int,
    val level: Int,
    /** Опыт до перехода на следующий уровень. */
    val experience: Int
) : Parcelable