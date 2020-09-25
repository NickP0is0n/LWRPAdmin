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
import androidx.appcompat.app.AppCompatActivity
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import kotlinx.android.synthetic.main.activity_main.*
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.*
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import live.nickp0is0n.lwrpadmin.ui.MenuActivity

class MainActivity : AppCompatActivity(), Observer {

    private var user: User = User("sample", "sample")

    private lateinit var receiver: DataReceiver
    private lateinit var queryClient: QueryClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableAutoUpdater()
    }

    override fun update(status: QueryStatus, resultType: QueryType) {
        if (status == QueryStatus.ERROR) {
            nameEdit.error = getString(R.string.server_error)
            progressBar.visibility = INVISIBLE
            return
        }
        when (resultType) {
            QueryType.CREDENTIALS -> updateCredentials()
            QueryType.ADMIN_INFO -> updateAdminInfo()
            else -> {
                nameEdit.error = getString(R.string.server_error)
                progressBar.visibility = INVISIBLE
            }
        }
    }

    fun onLoginButtonClick(view: View) {
        progressBar.visibility = VISIBLE
        checkCredentials()
    }

    private fun enableAutoUpdater() {
        val appUpdater = AppUpdater(this)
            .setUpdateFrom(UpdateFrom.GITHUB)
            .setGitHubUserAndRepo("NickP0is0n", "LWRPAdminUpdates")
        appUpdater.start()
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

    private fun updateCredentials() {
        val user = receiver.getData()
        if (user == null) {
            nameEdit.error = "Неправильный логин или пароль"
            progressBar.visibility = View.INVISIBLE
        }
        else {
            this.user = user as User
            getAdminInfo()
        }
    }

    private fun updateAdminInfo() {
        val admin = receiver.getData()
        if (admin == null) {
            nameEdit.error = "Данный пользователь не является администратором сервера"
            progressBar.visibility = View.INVISIBLE
        }
        else loadStatsForm(admin as Admin)
    }

    private fun getGlobalQueryVariables() : HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["username"] = nameEdit.text.toString()
        headers["password"] = passwordEdit.text.toString()
        return headers
    }
}