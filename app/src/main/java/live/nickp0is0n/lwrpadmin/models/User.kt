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
 * Данный класс содержит минимальную информацию,
 * необходимую для авторизации игрока в базе данных
 * сервера.
 * @property nickname
 * @property password
 */
@Parcelize
data class User (
    val nickname: String,
    val password: String
) : Parcelable