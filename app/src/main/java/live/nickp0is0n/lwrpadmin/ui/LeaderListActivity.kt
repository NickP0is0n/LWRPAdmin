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
import kotlinx.android.synthetic.main.activity_leader_list.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Leader

class LeaderListActivity : AppCompatActivity() {
    var leaders = ArrayList<Leader>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_list)
        initializeLeaderList()
        leaderList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            run {
                val intent = Intent(this, LeaderInfoActivity::class.java)
                intent.putExtra("leader", leaders[position])
                startActivity(intent)
            }
        }
        playTopBarAnimation()
    }

    private fun playTopBarAnimation() {
        leaderlisttopbar.x -= 1000f
        ObjectAnimator.ofFloat(leaderlisttopbar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun initializeLeaderList() {
        leaders = loadLeaders()
        val nicknamesList = ArrayList<String>()
        leaders.forEach {
            nicknamesList.add("[${resources.getStringArray(R.array.fractionNames)[it.fractionId]}] ${it.nickname} (${if(it.isOnline) "онлайн" else "оффлайн"})")
        }
        val adapter = ArrayAdapter<String>(this, R.layout.list_layout, nicknamesList)
        leaderList.adapter = adapter
    }

    private fun loadLeaders() : ArrayList<Leader> = intent.extras?.get("leaderList") as ArrayList<Leader>
}