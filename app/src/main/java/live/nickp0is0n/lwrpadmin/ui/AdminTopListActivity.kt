package live.nickp0is0n.lwrpadmin.ui

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_admin_top_list.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin

class AdminTopListActivity : AppCompatActivity() {
    private var admins = ArrayList<Admin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_top_list)
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
        adminListTopBar3.x -= 1000f
        ObjectAnimator.ofFloat(adminListTopBar3, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun initializeAdminList() {
        admins = loadAdmins()
        val nicknamesList = ArrayList<String>()
        admins.forEach {
            nicknamesList.add("[${it.adminLevel} уровень] ${it.nickname} (${it.totalOnline/60} минут отыграно)")
        }
        val adapter = ArrayAdapter<String>(this, R.layout.list_layout, nicknamesList)
        adminOnlineListView.adapter = adapter
    }

    private fun loadAdmins(): ArrayList<Admin> = intent.extras?.get("adminList") as ArrayList<Admin>
}