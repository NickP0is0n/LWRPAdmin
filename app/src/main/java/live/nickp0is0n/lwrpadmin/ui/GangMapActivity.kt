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
import kotlinx.android.synthetic.main.activity_gang_map.*
import live.nickp0is0n.lwrpadmin.R

class GangMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gang_map)
        playTopBarAnimation()
    }

    private fun playTopBarAnimation() {
        gangmaptopbar.x -= 1000f
        ObjectAnimator.ofFloat(gangmaptopbar, "translationX", 0f).apply {
            duration = 2000
            start()
        }
    }
}