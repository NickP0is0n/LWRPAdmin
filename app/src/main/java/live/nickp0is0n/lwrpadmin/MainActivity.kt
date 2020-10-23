/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import kotlinx.android.synthetic.main.activity_main.*
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.*
import live.nickp0is0n.lwrpadmin.service.Crypto
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus
import live.nickp0is0n.lwrpadmin.ui.MenuActivity

class MainActivity : AppCompatActivity(), Observer {

    private var user: User = User("sample", "sample")

    private lateinit var accountPreferences: SharedPreferences
    private lateinit var designPreferences: SharedPreferences

    private lateinit var receiver: DataReceiver
    private lateinit var queryClient: QueryClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableAutoUpdater()
        loadAppPreferences()
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

    fun onDarkThemeSwitchStateChanged(view: View) {
        if (darkThemeSwitch != null) {
            val editor = designPreferences.edit()
            when (darkThemeSwitch.isChecked) {
                true ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            editor.putInt("DarkMode", AppCompatDelegate.getDefaultNightMode()).apply()
        }
    }

    private fun loadAppPreferences() {
        accountPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
        designPreferences = getSharedPreferences("design", Context.MODE_PRIVATE)
        if (accountPreferences.contains("username")) {
            nameEdit.setText(accountPreferences.getString("username", "null"))
            val password = getPassword()
            passwordEdit.setText(password)
            progressBar.visibility = VISIBLE
            checkCredentials()
        }
        if (designPreferences.contains("DarkMode")) {
            val darkModeState = designPreferences.getInt("DarkMode", 0)
            AppCompatDelegate.setDefaultNightMode(darkModeState)
            if (darkThemeSwitch != null) {
                when (darkModeState) {
                    AppCompatDelegate.MODE_NIGHT_YES -> darkThemeSwitch.isChecked = true
                    AppCompatDelegate.MODE_NIGHT_NO -> darkThemeSwitch.isChecked = false
                }
            }

        }
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
        else {
            if (rememberMeBox.isChecked) {
                savePassword()
            }
            loadStatsForm(admin as Admin)
        }
    }

    private fun savePassword() {
        val editor = accountPreferences.edit()
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        val cryptoAgent = Crypto(deviceId)
        editor.putString("username", user.nickname)
            .putString("password", Crypto.byteArrayToHexString(cryptoAgent.encrypt(user.password.trim()))).apply()
    }

    private fun getPassword() : String {
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        val cryptoAgent = Crypto(deviceId)
        val encryptedPassword = accountPreferences.getString("password", "null")!!.trim()
        return String(cryptoAgent.decrypt(encryptedPassword)).filter { it.isLetterOrDigit() }
    }

    private fun getGlobalQueryVariables() : HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["username"] = nameEdit.text.toString()
        headers["password"] = passwordEdit.text.toString()
        return headers
    }
}