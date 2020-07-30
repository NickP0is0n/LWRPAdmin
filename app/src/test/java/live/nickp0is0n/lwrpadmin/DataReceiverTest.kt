/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin

import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.StatsReceiver
import live.nickp0is0n.lwrpadmin.network.UserCredentialsReceiver
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DataReceiverTest {
    @Test
    fun getData_retrieveSampleUserInfoFromJson_userObjectReturned() {
        val sampleUserInJson = "{\"nickname\":\"John_Smith\", \"password\":\"qwerty123\"}"
        val receiver = UserCredentialsReceiver()
        receiver.receiveData(JSONObject(sampleUserInJson))
        val rightUser = User("John_Smith", "qwerty123")
        val testUser = receiver.getData()
        assertEquals(rightUser, testUser)
    }

    @Test
    fun getData_retrieveSampleAdminInfoFromJson_adminObjectReturned() {
        val sampleAdminInJson = "{\"nickname\": \"John_Smith\",\n" +
                "\"adminLevel\": \"12\",\n" +
                "\"mondayOnline\": \"1\",\n" +
                "\"tuesdayOnline\": \"2\",\n" +
                "\"wednesdayOnline\": \"3\",\n" +
                "\"thursdayOnline\": \"4\",\n" +
                "\"fridayOnline\": \"5\",\n" +
                "\"saturdayOnline\": \"6\",\n" +
                "\"sundayOnline\": \"7\",\n" +
                "\"reportsAnswered\": \"100\",\n" +
                "\"muted\": \"200\",\n" +
                "\"jailed\": \"300\",\n" +
                "\"pAvig\": \"1\"}"
        val receiver = StatsReceiver()
        receiver.receiveData(JSONObject(sampleAdminInJson))
        val rightAdmin = Admin("John_Smith", 12, 1, 2, 3, 4, 5, 6, 7, 100, 200, 300, 1)
        val testAdmin = receiver.getData()
        assertEquals(rightAdmin, testAdmin)
    }

    @Test
    fun getData_retrieveSampleUserInfoWithNullUsernameFromJson_nullReturned() {
        val sampleUserInJson = "{\"nickname\": null, \"password\":\"qwerty123\"}"
        val receiver = UserCredentialsReceiver()
        receiver.receiveData(JSONObject(sampleUserInJson))
        val testUser = receiver.getData()
        assertNull(testUser)
    }

    @Test
    fun getData_retrieveSampleAdminInfoWithNullUsernameFromJson_nullReturned() {
        val sampleAdminInJson = "{\"nickname\": \"null\",\n" +
                "\"adminLevel\": \"12\",\n" +
                "\"mondayOnline\": \"1\",\n" +
                "\"tuesdayOnline\": \"2\",\n" +
                "\"wednesdayOnline\": \"3\",\n" +
                "\"thursdayOnline\": \"4\",\n" +
                "\"fridayOnline\": \"5\",\n" +
                "\"saturdayOnline\": \"6\",\n" +
                "\"sundayOnline\": \"7\",\n" +
                "\"reportsAnswered\": \"100\",\n" +
                "\"muted\": \"200\",\n" +
                "\"jailed\": \"300\",\n" +
                "\"pAvig\": \"1\"}"
        val receiver = StatsReceiver()
        receiver.receiveData(JSONObject(sampleAdminInJson))
        val testAdmin = receiver.getData()
        assertNull(testAdmin)
    }
}
