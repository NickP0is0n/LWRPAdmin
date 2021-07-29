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
 * Базовая информация об администраторе сервера,
 * которая отображается в статистике.
 * @property nickname
 * @property adminLevel
 * @property mondayOnline
 * @property tuesdayOnline
 * @property wednesdayOnline
 * @property thursdayOnline
 * @property fridayOnline
 * @property saturdayOnline
 * @property sundayOnline
 * @property reportsAnswered
 * @property muted
 * @property jailed
 * @property pAvig
 */
@Parcelize
data class Admin(
    val nickname: String,
    val adminLevel: Int,
    val mondayOnline: Int = 0,
    val tuesdayOnline: Int = 0,
    val wednesdayOnline: Int = 0,
    val thursdayOnline: Int = 0,
    val fridayOnline: Int = 0,
    val saturdayOnline: Int = 0,
    val sundayOnline: Int = 0,
    val reportsAnswered: Int,
    val muted: Int,
    val jailed: Int,
    val pAvig: Int
) : Parcelable {
    val totalOnline
    get() = mondayOnline + tuesdayOnline + wednesdayOnline + thursdayOnline + fridayOnline + saturdayOnline + sundayOnline

    companion object {
        const val ONLINE_REQUIREMENT = 180 // в минутах
    }
}