/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ip_info.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin
import live.nickp0is0n.lwrpadmin.models.IpInfo
import live.nickp0is0n.lwrpadmin.models.User
import live.nickp0is0n.lwrpadmin.network.IpInfoReceiver
import live.nickp0is0n.lwrpadmin.network.QueryClient
import live.nickp0is0n.lwrpadmin.network.QueryType
import live.nickp0is0n.lwrpadmin.service.Observer
import live.nickp0is0n.lwrpadmin.service.QueryStatus

class IpInfoActivity : AppCompatActivity(), Observer {
    private var user: User? = null
    private var admin: Admin? = null

    private var receiver = IpInfoReceiver()
    private lateinit var queryClient: QueryClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_info)
        user = intent.extras?.get("user") as User
        admin = intent.extras?.get("adminInfo") as Admin
        queryClient = intent.extras?.get("queryClient") as QueryClient
        playTopBarAnimation()
    }

    override fun update(status: QueryStatus, resultType: QueryType) {
        if (status == QueryStatus.ERROR) {
            Toast.makeText(this@IpInfoActivity, "Ошибка сервера, повторите позже.", Toast.LENGTH_SHORT).show()
            progressBar4.visibility = View.INVISIBLE
        }
        else loadIpInfo()
    }

    fun onRetrieveInfoButtonClick(view: View) {
        progressBar4.visibility = View.VISIBLE
        requestIpInfo()
    }

    private fun requestIpInfo() {
        val currentLocalParams = HashMap<String, String>()
        currentLocalParams["player_username"] = nameEdit3.text.toString()
        receiver.notifier.addObserver(this@IpInfoActivity)
        queryClient.executeQuery(context = this, scriptPath = "getPlayerIP.php", localParams = currentLocalParams, dataReceiver = receiver, isResponseArray = false)
    }

    private fun loadIpInfo() {
        val ipInfoList = receiver.getData()
        if (ipInfoList == null) {
            runOnUiThread{
                Toast.makeText(this@IpInfoActivity, getString(R.string.player_not_found), Toast.LENGTH_SHORT).show()
                progressBar4.visibility = View.INVISIBLE
            }
        }
        else {
            displayIpInfo(ipInfoList)
            showHiddenLabels()
            progressBar4.visibility = View.INVISIBLE
        }
    }

    private fun displayIpInfo(ipInfoList: List<IpInfo>) {
        runOnUiThread {
            regIp.text = ipInfoList[0].ip
            regCountry.text = ipInfoList[0].country
            regCity.text = ipInfoList[0].city
            regProvider.text = ipInfoList[0].provider

            currentIp.text = ipInfoList[1].ip
            currentCountry.text = ipInfoList[1].country
            currentCity.text = ipInfoList[1].city
            currentProvider.text = ipInfoList[1].provider
        }
    }

    private fun showHiddenLabels() {
        runOnUiThread {
            regIp.visibility = View.VISIBLE
            regCountry.visibility = View.VISIBLE
            regCity.visibility = View.VISIBLE
            regProvider.visibility = View.VISIBLE

            regIpLabel.visibility = View.VISIBLE
            regCountryLabel.visibility = View.VISIBLE
            regCityLabel.visibility = View.VISIBLE
            regProviderLabel.visibility = View.VISIBLE

            currentIp.visibility = View.VISIBLE
            currentCountry.visibility = View.VISIBLE
            currentCity.visibility = View.VISIBLE
            currentProvider.visibility = View.VISIBLE

            currentIpLabel.visibility = View.VISIBLE
            currentCountryLabel.visibility = View.VISIBLE
            currentCityLabel.visibility = View.VISIBLE
            currentProviderLabel.visibility = View.VISIBLE

            regData.visibility = View.VISIBLE
            currentData.visibility = View.VISIBLE
        }
    }

    private fun playTopBarAnimation() {
        ipinfotopbar.y -= 1000f
        ObjectAnimator.ofFloat(ipinfotopbar, "translationY", 0f).apply {
            duration = 1000
            start()
        }
    }
}