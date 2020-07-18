/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_menu.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.Leader
import live.nickp0is0n.lwrpadmin.models.User
import org.json.JSONArray

class MenuActivity : AppCompatActivity() {
    private var user: User? = null
    private var admin: Admin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        user = intent.extras?.get("user") as User
        admin = intent.extras?.get("adminInfo") as Admin
        playMainMenuBarAnimation()
    }

    private fun playMainMenuBarAnimation() {
        mainmenubar.x -= 1000f
        ObjectAnimator.ofFloat(mainmenubar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    fun onLeaderListButtonClick(view: View) {
        progressBar2.visibility = VISIBLE
        requestLeaderInfo()
    }

    fun onAdminOnlineListButtonClick(view: View) {
        progressBar2.visibility = VISIBLE
        requestAdminInfo()
    }

    fun onChangeAdminPasswordButtonClick(view: View) {
        val intent = Intent(this, AdminPasswordChangeActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    fun onMyStatsButtonClick(view: View) {
        val intent = Intent(this, StatsActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("adminInfo", admin)
        startActivity(intent)
    }

    private fun requestLeaderInfo() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwrp.ru/service/functions/hidden/fu3u8w2/getLeaderInfo.php"
        var leaders = ArrayList<Leader>()

        val leaderListRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                run {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        leaders.add(Leader(
                            nickname = array.getJSONArray(i).getString(0),
                            fractionId = array.getJSONArray(i).getInt(1),
                            getOnDate = array.getJSONArray(i).getString(2),
                            isOnline = array.getJSONArray(i).getInt(3) != 0,
                            playersInFractionOnline = array.getJSONArray(i).getInt(4),
                            level = array.getJSONArray(i).getInt(5),
                            experience = array.getJSONArray(i).getInt(6)
                        ))
                    }
                    val intent = Intent(this, LeaderListActivity::class.java)
                    intent.putExtra("leaderList", leaders)
                    progressBar2.visibility = INVISIBLE
                    startActivity(intent)
                }
            },
            Response.ErrorListener {
                Toast.makeText(this@MenuActivity, "Ошибка сервера: $it", Toast.LENGTH_SHORT).show()
                progressBar2.visibility = INVISIBLE
            }) {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["username"] = user!!.nickname
                headers["password"] = user!!.password
                return headers
            }
        }
        queue.add(leaderListRequest)
    }

    private fun requestAdminInfo() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwrp.ru/service/functions/hidden/fu3u8w2/getAdminListAndInfo.php"
        val admins = ArrayList<Admin>()

        val adminListRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                run {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        admins.add(Admin(
                            nickname = array.getJSONArray(i).getString(0),
                            adminLevel = array.getJSONArray(i).getInt(1),
                            mondayOnline = array.getJSONArray(i).getInt(2),
                            tuesdayOnline = array.getJSONArray(i).getInt(3),
                            wednesdayOnline = array.getJSONArray(i).getInt(4),
                            thursdayOnline = array.getJSONArray(i).getInt(5),
                            fridayOnline = array.getJSONArray(i).getInt(6),
                            saturdayOnline = array.getJSONArray(i).getInt(7),
                            sundayOnline = array.getJSONArray(i).getInt(8),
                            reportsAnswered = array.getJSONArray(i).getInt(9),
                            muted = array.getJSONArray(i).getInt(10),
                            jailed = array.getJSONArray(i).getInt(11),
                            awarnCount = array.getJSONArray(i).getInt(12)
                        ))
                    }
                    val intent = Intent(this, AdminListActivity::class.java)
                    intent.putExtra("adminList", admins)
                    progressBar2.visibility = INVISIBLE
                    startActivity(intent)
                }
            },
            Response.ErrorListener {
                Toast.makeText(this@MenuActivity, "Ошибка сервера: $it", Toast.LENGTH_SHORT).show()
                progressBar2.visibility = INVISIBLE
            }) {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["username"] = user!!.nickname
                headers["password"] = user!!.password
                return headers
            }
        }
        queue.add(adminListRequest)
    }

}