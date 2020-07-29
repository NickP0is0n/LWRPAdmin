/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class QueryClient(val basicUrl: String, val globalParams: HashMap<String, String>) {
    fun executeQuery(context: Context, scriptPath: String, localParams: HashMap<String, String>? = null, dataReceiver: DataReceiver) {
        val queue = Volley.newRequestQueue(context)
        var result: Any?

        val loginRequest = object : StringRequest(
            Request.Method.POST, basicUrl + scriptPath,
            Response.Listener { response ->
                run {
                    val obj = JSONObject(response)
                    dataReceiver.receiveData(obj)
                }
            },
            Response.ErrorListener {
                dataReceiver.errorCallback("Ошибка сервера, повторите позже")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val headers = globalParams.clone() as HashMap<String, String>
                localParams?.forEach { index, value ->
                    headers[index] = value
                }
                return headers
            }
        }
    }
}