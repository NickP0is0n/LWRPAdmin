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
import kotlinx.android.synthetic.main.activity_menu.*
import live.nickp0is0n.lwrpadmin.MainActivity
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.GangMap
import live.nickp0is0n.lwrpadmin.models.Leader
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.*
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus

class MenuActivity : AppCompatActivity(), Observer {
    private var user: User? = null
    private var admin: Admin? = null

    private lateinit var receiver: DataReceiver
    private lateinit var queryClient: QueryClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        user = intent.extras?.get("user") as User
        admin = intent.extras?.get("adminInfo") as Admin
        queryClient = QueryClient("https://lwrp.ru/service/functions/hidden/fu3u8w2/", getGlobalQueryVariables())
        playMainMenuBarAnimation()
    }

    override fun update(status: QueryStatus, resultType: QueryType) {
        if (status == QueryStatus.ERROR) {
            Toast.makeText(this@MenuActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
            progressBar2.visibility = INVISIBLE
            return
        }
        when (resultType) {
            QueryType.LEADER_LIST -> loadLeaders()
            QueryType.ADMIN_LIST -> loadAdmins()
            QueryType.GANG_MAP -> loadGangMap()
            else -> Toast.makeText(this@MenuActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
        }
    }

    fun onExitButtonClick(view: View) {
        val accountPreferences = getSharedPreferences("account", MODE_PRIVATE)
        val editor = accountPreferences.edit()
        editor.remove("username").remove("password").apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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

    fun onIpInfoButtonClick(view: View) {
        val intent = Intent(this, IpInfoActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("adminInfo", admin)
        intent.putExtra("queryClient", queryClient)
        startActivity(intent)
    }

    fun onMyStatsButtonClick(view: View) {
        val intent = Intent(this, StatsActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("adminInfo", admin)
        startActivity(intent)
    }

    fun onGangMapButtonClick(view: View) {
        progressBar2.visibility = VISIBLE
        requestGangMap()
    }

    private fun playMainMenuBarAnimation() {
        mainmenubar.x -= 1000f
        ObjectAnimator.ofFloat(mainmenubar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun requestGangMap() {
        receiver = GangMapReceiver()
        (receiver as GangMapReceiver).notifier.addObserver(this@MenuActivity)
        queryClient.executeQuery(context = this, scriptPath = "getGangMap.php", dataReceiver = receiver, isResponseArray = false)
    }

    private fun requestLeaderInfo() {
        receiver = LeadersReceiver()
        (receiver as LeadersReceiver).notifier.addObserver(this@MenuActivity)
        queryClient.executeQuery(context = this, scriptPath = "getLeaderInfo.php", dataReceiver = receiver, isResponseArray = true)
    }

    private fun requestAdminInfo() {
        receiver = AdminListReceiver()
        (receiver as AdminListReceiver).notifier.addObserver(this@MenuActivity)
        queryClient.executeQuery(context = this, scriptPath = "getAdminListAndInfo.php", dataReceiver = receiver, isResponseArray = true)
    }

    private fun getGlobalQueryVariables() : HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["username"] = user!!.nickname
        headers["password"] = user!!.password
        return headers
    }

    private fun loadGangMap() {
        val gangMap = receiver.getData() as GangMap
        val intent = Intent(this, GangMapActivity::class.java)
        intent.putExtra("gangMap", gangMap)
        progressBar2.visibility = INVISIBLE
        startActivity(intent)
    }

    private fun loadAdmins() {
        val admins = receiver.getData() as ArrayList<Admin>
        val intent = Intent(this, AdminListActivity::class.java)
        intent.putExtra("adminList", admins)
        progressBar2.visibility = INVISIBLE
        startActivity(intent)
    }

    private fun loadLeaders() {
        val leaders = receiver.getData() as ArrayList<Leader>
        val intent = Intent(this, LeaderListActivity::class.java)
        intent.putExtra("leaderList", leaders)
        progressBar2.visibility = INVISIBLE
        startActivity(intent)
    }

}