/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import kotlinx.android.synthetic.main.activity_main.*
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.ui.MenuActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var user: User = User("sample", "sample")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar?.hide() //hide the title bar
        setContentView(R.layout.activity_main)
        enableAutoUpdater()
    }

    private fun enableAutoUpdater() {
        val appUpdater = AppUpdater(this)
            .setUpdateFrom(UpdateFrom.GITHUB)
            .setGitHubUserAndRepo("NickP0is0n", "LWRPAdminUpdates")
        appUpdater.start()
    }

    fun onLoginButtonClick(view: View) {
        progressBar.visibility = VISIBLE
        checkCredentials()
    }

    private fun getAdminInfo() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwrp.ru/service/functions/hidden/fu3u8w2/getAccountInfo1.php"
        var admin: Admin

        val loginRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                run {
                    val obj = JSONObject(response)
                    if (obj.getString("nickname") == "null") {
                        nameEdit.error = "Данный пользователь не является администратором сервера"
                        progressBar.visibility = INVISIBLE
                    }
                    admin = Admin(
                        obj.getString("nickname"),
                        obj.getInt("adminLevel"),
                        obj.getInt("mondayOnline"),
                        obj.getInt("tuesdayOnline"),
                        obj.getInt("wednesdayOnline"),
                        obj.getInt("thursdayOnline"),
                        obj.getInt("fridayOnline"),
                        obj.getInt("saturdayOnline"),
                        obj.getInt("sundayOnline"),
                        obj.getInt("reportsAnswered"),
                        obj.getInt("muted"),
                        obj.getInt("jailed"),
                        obj.getInt("pAvig")
                    )
                    loadStatsForm(admin)
                }
            },
            Response.ErrorListener {
                nameEdit.error = "Ошибка сервера, повторите позже"
                progressBar.visibility = INVISIBLE
            }) {
                    override fun getParams(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["username"] = nameEdit.text.toString()
                        headers["password"] = passwordEdit.text.toString()
                        headers["admin_username"] = nameEdit.text.toString()
                        return headers
            }
        }

        queue.add(loginRequest)
    }
    
    private fun loadStatsForm(admin: Admin) {
        progressBar.visibility = INVISIBLE
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("adminInfo", admin)
        startActivity(intent)
    }

    private fun checkCredentials() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwrp.ru/service/functions/hidden/fu3u8w2/getAccountInfo.php"

        val loginRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                run {
                    val obj = JSONObject(response)
                    if (obj.isNull("nickname")) {
                        nameEdit.error = "Неправильный логин или пароль"
                    }
                    else {
                        user = User(obj.getString("nickname"), obj.getString("password"))
                        getAdminInfo()
                    }
                }
            },
            Response.ErrorListener {
                nameEdit.error = "Ошибка сервера, повторите позже"
                progressBar.visibility = INVISIBLE
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["username"] = nameEdit.text.toString()
                headers["password"] = passwordEdit.text.toString()
                return headers
            }
        }
        queue.add(loginRequest)
    }
}