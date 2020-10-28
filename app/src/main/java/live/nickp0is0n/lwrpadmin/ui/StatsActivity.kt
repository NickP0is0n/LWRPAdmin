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
import android.view.View.VISIBLE
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stats.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.Admin
import java.util.*
import java.util.Calendar.DAY_OF_WEEK

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar?.hide() //hide the title bar
        setContentView(R.layout.activity_stats)
        val admin = intent.extras?.get("adminInfo") as Admin
        initializeGUI(admin)
        playHeaderAnimation()
    }

    private fun playHeaderAnimation() {
        view3.x -= 1000f
        ObjectAnimator.ofFloat(view3, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }

    private fun initializeGUI(admin: Admin) {
        initializeBasicInfo(admin)
        initializeOnlineTimes(admin)
        initializeExecution(admin)
    }

    private fun initializeBasicInfo(admin: Admin) {
        nickname.text = admin.nickname
        adminLevel.text = admin.adminLevel.toString()
    }

    private fun initializeOnlineTimes(admin: Admin) {
        monday.text = getString(R.string.time_template, admin.mondayOnline / 60 / 60, (admin.mondayOnline / 60) % 60)
        tuesday.text = getString(R.string.time_template, admin.tuesdayOnline / 60 / 60, (admin.tuesdayOnline / 60) % 60)
        wednesday.text = getString(R.string.time_template, admin.wednesdayOnline / 60 / 60, (admin.wednesdayOnline / 60) % 60)
        thursday.text = getString(R.string.time_template, admin.thursdayOnline / 60 / 60, (admin.thursdayOnline / 60) % 60)
        friday.text = getString(R.string.time_template, admin.fridayOnline / 60 / 60, (admin.fridayOnline / 60) % 60)
        saturday.text = getString(R.string.time_template, admin.saturdayOnline / 60 / 60, (admin.saturdayOnline / 60) % 60)
        sunday.text = getString(R.string.time_template, admin.sundayOnline / 60 / 60, (admin.sundayOnline / 60) % 60)
        if(checkRequiredTime(admin)) gametimeNorm.visibility = VISIBLE
    }

    private fun checkRequiredTime(admin: Admin) : Boolean =  when(Calendar.getInstance().get(DAY_OF_WEEK)) {
        1 -> admin.sundayOnline < 14400 //Класс Calendar использует воскресенье в качестве первого дня
        2 -> admin.mondayOnline < 14400
        3 -> admin.tuesdayOnline < 14400
        4 -> admin.wednesdayOnline < 14400
        5 -> admin.thursdayOnline < 14400
        6 -> admin.fridayOnline < 14400
        7 -> admin.saturdayOnline < 14400
        else -> false
    }

    private fun initializeExecution(admin: Admin) {
        reportCount.text = getString(R.string.count_template, admin.reportsAnswered)
        muteCount.text = getString(R.string.count_template, admin.muted)
        jailCount.text = getString(R.string.count_template, admin.jailed)
        warnCount.text = "${admin.pAvig}/3"
    }
}