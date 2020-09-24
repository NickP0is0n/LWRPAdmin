/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.network

import android.content.Context
import android.os.Parcelable
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
class QueryClient(val basicUrl: String, val globalParams: HashMap<String, String>) : Parcelable {
    fun executeQuery(context: Context, scriptPath: String, localParams: HashMap<String, String>? = null, dataReceiver: DataReceiver, isResponseArray: Boolean = false) {
        val queue = Volley.newRequestQueue(context)
        var result: Any?

        val request = object : StringRequest(
            Method.POST, basicUrl + scriptPath,
            Response.Listener { response ->
                run {
                    val obj: Any = if (isResponseArray) JSONArray(response)
                    else JSONObject(response)
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
        queue.add(request)
    }
}