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
import live.nickp0is0n.lwrpadmin.models.GangMap

class GangMapActivity : AppCompatActivity() {
    private lateinit var gangMap: GangMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gang_map)
        gangMap = intent.extras?.get("gangMap") as GangMap
        initializeGangMapUI()
        playTopBarAnimation()
    }

    private fun initializeGangMapUI() {
        grovePlayerCount.text = gangMap.groveOnline.toString()
        groveTerritoryCount.text = gangMap.groveTerritories.toString()
        ballasPlayerCount.text = gangMap.ballasOnline.toString()
        ballasTerritoryCount.text = gangMap.ballasTerritories.toString()
        vagosPlayerCount.text = gangMap.vagosOnline.toString()
        vagosTerritoryCount.text = gangMap.vagosTerritories.toString()
        rifaPlayerCount.text = gangMap.rifaOnline.toString()
        rifaTerritoryCount.text = gangMap.rifaTerritories.toString()
        aztecPlayerCount.text = gangMap.aztecOnline.toString()
        aztecTerritoryCount.text = gangMap.aztecTerritories.toString()
        lastUpdateDate.text = gangMap.lastUpdate
    }

    private fun playTopBarAnimation() {
        gangmaptopbar.y -= 1000f
        ObjectAnimator.ofFloat(gangmaptopbar, "translationY", 0f).apply {
            duration = 1000
            start()
        }
    }
}