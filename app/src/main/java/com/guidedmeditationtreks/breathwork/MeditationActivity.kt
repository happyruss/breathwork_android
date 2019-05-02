package com.guidedmeditationtreks.breathwork

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton

import com.guidedmeditationtreks.breathwork.managers.BreathworkManager
import com.guidedmeditationtreks.breathwork.models.TrackDelegate

import java.util.Locale

import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class MeditationActivity : AppCompatActivity(), TrackDelegate, SeekBar.OnSeekBarChangeListener {

    private var playPauseButton: ToggleButton? = null
    private var timerTextView: TextView? = null
    private var meditationNameTextView: TextView? = null

    private var breathVolumeSeekBar: SeekBar? = null
    private var breathSpeedSeekBar: SeekBar? = null
    private var voiceVolumeSeekBar: SeekBar? = null
    private var musicVolumeSeekBar: SeekBar? = null

    private var isInMeditation = false
    private val breathworkManager = BreathworkManager.singleton

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)
        breathworkManager.setDelegate(this)
        connectView()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sf-pro-text-semibold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        if (!(breathworkManager.inMeditation)!!) { //workaround for screen rotation
            val intent = intent
            val gapAmount = intent.getIntExtra("gapAmount", 0)
            breathworkManager.playActiveTrackFromBeginning()
            breathworkManager.inMeditation = true
        }
        if (!(breathworkManager?.trackCompleted)!!) {
            isInMeditation = true
            breathworkManager.userStartedTrack()
        }
    }

    private fun closeActivity() {
        breathworkManager.clearCurrentTrack()
        finish()
    }

    override fun onBackPressed() {
        if (isInMeditation) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Meditation Underway")
            alertDialogBuilder
                    .setMessage("Would you like to stop the current session?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id -> closeActivity() }
                    .setNegativeButton("No") { dialog, id -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            closeActivity()
        }
    }

    fun didTapBackButton(v: View) {
        this.onBackPressed()
    }

    private fun connectView() {
        playPauseButton = findViewById(R.id.playPauseButton)
        playPauseButton!!.visibility = View.VISIBLE
        timerTextView = findViewById(R.id.timerTextView)
        meditationNameTextView = findViewById(R.id.meditationNameTextView)
        meditationNameTextView!!.text = breathworkManager.activeTrackName
        meditationNameTextView!!.setShadowLayer(2.0f, 2.0f, 2.0f, R.color.colorShadow)
        playPauseButton!!.z = 20f

        breathVolumeSeekBar = findViewById(R.id.breathVolumeSeekBar)
        breathVolumeSeekBar!!.setOnSeekBarChangeListener(this)
        breathSpeedSeekBar = findViewById(R.id.breathSpeedSeekBar)
        breathSpeedSeekBar!!.setOnSeekBarChangeListener(this)
        voiceVolumeSeekBar = findViewById(R.id.voiceVolumeSeekBar)
        voiceVolumeSeekBar!!.setOnSeekBarChangeListener(this)
        musicVolumeSeekBar = findViewById(R.id.musicVolumeSeekBar)
        musicVolumeSeekBar!!.setOnSeekBarChangeListener(this)

        breathVolumeSeekBar!!.progress = (breathworkManager.user.savedBreathVolume * 100.0f).toInt()
        breathSpeedSeekBar!!.progress = (breathworkManager.user.savedBreathSpeed * 100.0f).toInt()
        voiceVolumeSeekBar!!.progress = (breathworkManager.user.savedVoiceVolume * 100.0f).toInt()
        musicVolumeSeekBar!!.progress = (breathworkManager.user.savedMusicVolume * 100.0f).toInt()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        // called when progress is changed
        val floatProgress = progress.toFloat() / 100
        if (seekBar == breathVolumeSeekBar) {
            breathworkManager.setBreathVolume(floatProgress)
            return
        }
        if (seekBar == breathSpeedSeekBar) {
            breathworkManager.setBreathSpeed(floatProgress)
            return
        }
        if (seekBar == voiceVolumeSeekBar) {
            breathworkManager.setVoiceVolume(floatProgress)
            return
        }
        if (seekBar == musicVolumeSeekBar) {
            breathworkManager.setMusicVolume(floatProgress)
            return
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is started
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is stopped
    }

    fun didTapPlayPause(v: View) {
        breathworkManager.pauseOrResume()
    }

    override fun trackTimeRemainingUpdated(timeRemaining: Int) {
        breathworkManager.incrementTotalSecondsInMeditation()
        val timeRemainingString = String.format(Locale.getDefault(), "%01d:%02d", timeRemaining / 60, timeRemaining % 3600 % 60)
        timerTextView!!.setText(timeRemainingString)
    }

    override fun trackEnded() {
        breathworkManager.userCompletedTrack()
        playPauseButton!!.visibility = View.INVISIBLE
        isInMeditation = false
        breathworkManager.trackCompleted = true
        closeActivity()
    }

}
