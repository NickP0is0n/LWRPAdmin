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
 * Информация о составе банд и их территориях
 *
 * @property groveTerritories
 * @property groveOnline
 * @property ballasTerritories
 * @property ballasOnline
 * @property vagosTerritories
 * @property vagosOnline
 * @property rifaTerritories
 * @property rifaOnline
 * @property aztecTerritories
 * @property aztecOnline
 * @property lastUpdate
 */
@Parcelize
data class GangMap (
    val groveTerritories: Int,
    val groveOnline: Int,
    val ballasTerritories: Int,
    val ballasOnline: Int,
    val vagosTerritories: Int,
    val vagosOnline: Int,
    val rifaTerritories: Int,
    val rifaOnline: Int,
    val aztecTerritories: Int,
    val aztecOnline: Int,
    val lastUpdate: String
) : Parcelable