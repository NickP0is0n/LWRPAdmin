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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_leader_info.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Leader

class LeaderInfoActivity : AppCompatActivity() {
    private lateinit var leader: Leader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_info)
        leader = intent.extras?.get("leader") as Leader
        initializeStats()
        playTopBarAnimation()
    }

    private fun playTopBarAnimation() {
        leaderinfotopbar.x -= 1000f
        ObjectAnimator.ofFloat(leaderinfotopbar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun initializeStats() {
        nickname2.text = leader.nickname
        status.text = if(leader.isOnline) "Онлайн" else "Оффлайн"
        status.setTextColor(if(leader.isOnline) ContextCompat.getColor(this, android.R.color.holo_green_dark) else ContextCompat.getColor(this, android.R.color.holo_red_dark))
        fractionName.text = resources.getStringArray(R.array.fractionNames)[leader.fractionId]
        lastGetOn.text = leader.getOnDate
        fractionPlayerCount.text = leader.playersInFractionOnline.toString()
        level.text = leader.level.toString()
        level2.text = "${leader.experience}/${(leader.level + 1) * 4}"
    }
}