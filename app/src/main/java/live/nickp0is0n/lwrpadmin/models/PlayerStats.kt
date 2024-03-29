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
 * Данный класс содержит статистику обычного игрока.
 */

@Parcelize
data class PlayerStats (
    val nickname: String,
    val level: Int,
    val lawLevel: Int, // законопослушность
    val warns: Int,
    val drugAddiction: Int,
    val fraction: Int = 0,
    val rank: Int = 0,
    val house: Int,
    val cars: String,
    val business: Int
) : Parcelable