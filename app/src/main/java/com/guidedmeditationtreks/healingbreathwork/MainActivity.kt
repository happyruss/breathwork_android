package com.guidedmeditationtreks.healingbreathwork

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.guidedmeditationtreks.healingbreathwork.managers.BreathworkManager
import com.guidedmeditationtreks.healingbreathwork.model.TrackDelegate
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity(), TrackDelegate {

    private val breathworkManager = BreathworkManager.singleton

    private var startButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        connectView()
        breathworkManager.activeTrack?.delegate = this
    }

    fun didTapStartButton(view: View) {
        breathworkManager.initTrack(1, this.applicationContext)
        breathworkManager.activeTrack?.playFromBeginning()
    }

    private fun connectView() {
        startButton = findViewById(R.id.startButton)
    }

    override fun trackTimeRemainingUpdated(timeRemaining: Int) {
//        vipassanaManager.incrementTotalSecondsInMeditation()
        val timeRemainingString =
            String.format(Locale.getDefault(), "%01d:%02d", timeRemaining / 60, timeRemaining % 3600 % 60)
        timerTextView.text = timeRemainingString
    }

    override fun trackEnded() {
//        vipassanaManager.userCompletedTrack()
//        playPauseButton.setVisibility(View.INVISIBLE)
//        isInMeditation = false
//        vipassanaManager.setTrackCompleted(true)
    }

}
