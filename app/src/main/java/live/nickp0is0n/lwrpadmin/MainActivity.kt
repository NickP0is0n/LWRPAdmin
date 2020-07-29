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
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import kotlinx.android.synthetic.main.activity_main.*
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.DataReceiver
import live.nickp0is0n.lwrpadmin.network.QueryClient
import live.nickp0is0n.lwrpadmin.network.StatsReceiver
import live.nickp0is0n.lwrpadmin.network.UserCredentialsReceiver
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import live.nickp0is0n.lwrpadmin.ui.MenuActivity

class MainActivity : AppCompatActivity(), Observer {
    var user: User = User("sample", "sample")

    private lateinit var receiver: DataReceiver
    private lateinit var queryClient: QueryClient

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
        receiver = StatsReceiver()
        (receiver as StatsReceiver).notifier.addObserver(this@MainActivity)
        val localQueryVariables = HashMap<String, String>()
        localQueryVariables["admin_username"] = nameEdit.text.toString()
        queryClient.executeQuery(this, "getAccountInfo1.php", localQueryVariables, receiver)
    }
    
    private fun loadStatsForm(admin: Admin) {
        progressBar.visibility = INVISIBLE
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("adminInfo", admin)
        startActivity(intent)
    }

    private fun checkCredentials() {
        queryClient = QueryClient("https://lwrp.ru/service/functions/hidden/fu3u8w2/", getGlobalQueryVariables())
        receiver = UserCredentialsReceiver()
        (receiver as UserCredentialsReceiver).notifier.addObserver(this@MainActivity)
        queryClient.executeQuery(context = this, scriptPath = "getAccountInfo.php", dataReceiver = receiver)
    }

    override fun update(status: QueryStatus, queueName: String) {
        if (status == QueryStatus.ERROR) {
            nameEdit.error = "Ошибка сервера, повторите позже"
            progressBar.visibility = INVISIBLE
            return
        }
        when (queueName) {
            "credentials" -> updateCredentials()
            "adminInfo" -> updateAdminInfo()
        }
    }

    private fun updateCredentials() {
        val user: User
        val responseObject = (receiver as UserCredentialsReceiver).data
        if (responseObject!!.isNull("nickname")) {
            nameEdit.error = "Неправильный логин или пароль"
        }
        else {
            user = User(responseObject.getString("nickname"), responseObject.getString("password"))
            getAdminInfo()
        }
    }

    private fun updateAdminInfo() {
        val admin: Admin
        val responseObject = (receiver as StatsReceiver).data
        if (responseObject!!.getString("nickname") == "null") {
            nameEdit.error = "Данный пользователь не является администратором сервера"
            progressBar.visibility = INVISIBLE
        }
        admin = Admin(
            responseObject.getString("nickname"),
            responseObject.getInt("adminLevel"),
            responseObject.getInt("mondayOnline"),
            responseObject.getInt("tuesdayOnline"),
            responseObject.getInt("wednesdayOnline"),
            responseObject.getInt("thursdayOnline"),
            responseObject.getInt("fridayOnline"),
            responseObject.getInt("saturdayOnline"),
            responseObject.getInt("sundayOnline"),
            responseObject.getInt("reportsAnswered"),
            responseObject.getInt("muted"),
            responseObject.getInt("jailed"),
            responseObject.getInt("pAvig")
        )
        loadStatsForm(admin)
    }

    private fun getGlobalQueryVariables() : HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["username"] = nameEdit.text.toString()
        headers["password"] = passwordEdit.text.toString()
        return headers
    }
}