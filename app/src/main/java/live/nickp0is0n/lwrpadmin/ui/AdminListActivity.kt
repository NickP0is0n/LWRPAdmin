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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin_list.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin

class AdminListActivity : AppCompatActivity() {
    private var admins = ArrayList<Admin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_list)
        initializeAdminList()
        adminOnlineListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            run {
                val intent = Intent(this, StatsActivity::class.java)
                intent.putExtra("adminInfo", admins[position])
                startActivity(intent)
            }
        }
        playTopBarAnimation()
    }

    private fun playTopBarAnimation() {
        adminListTopBar.x -= 1000f
        ObjectAnimator.ofFloat(adminListTopBar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun initializeAdminList() {
        admins = loadAdmins()
        val nicknamesList = ArrayList<String>()
        admins.forEach {
            nicknamesList.add("[${it.adminLevel} уровень] ${it.nickname} (${it.awarnCount} предупреждений)")
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nicknamesList)
        adminOnlineListView.adapter = adapter
    }

    private fun loadAdmins(): ArrayList<Admin> = intent.extras?.get("adminList") as ArrayList<Admin>
}