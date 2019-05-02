package com.guidedmeditationtreks.breathwork.managers

import android.content.Context
import android.content.SharedPreferences

import com.guidedmeditationtreks.breathwork.models.Track
import com.guidedmeditationtreks.breathwork.models.TrackDelegate
import com.guidedmeditationtreks.breathwork.models.TrackTemplate
import com.guidedmeditationtreks.breathwork.models.User

/**
 * Created by czar on 12/22/17.
 * Manages the breathwork tasks
 */

class BreathworkManager private constructor() {
    private var settings: SharedPreferences? = null
    private val trackTemplateFactory = TrackTemplateFactory.singleton
    val user: User
    private var activeTrack: Track? = null
    private var activeTrackLevel: Int = 0
    var inMeditation: Boolean? = null
    var trackCompleted: Boolean? = null

    var savedBreathVolume: Float = 0.5f
    var savedBreathSpeed: Float = 0.5f
    var savedVoiceVolume: Float = 0.5f
    var savedMusicVolume: Float = 0.5f

    val userTotalSecondsInMeditation: Int
        get() = user.totalSecondsInMeditation

    val activeTrackName: String?
        get() = if (activeTrack == null) {
            null
        } else activeTrack!!.name

    init {
        this.user = User()
    }

    fun initTrackAtLevel(trackLevel: Int, context: Context) {
        this.clearCurrentTrack()
        this.activeTrackLevel = trackLevel
        val trackTemplate = trackTemplateFactory.getTrackTemplate(trackLevel)
        this.activeTrack = Track(trackTemplate, context, 1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun playActiveTrackFromBeginning() {
        this.activeTrack!!.playFromBeginning()
    }

    fun pauseOrResume() {
        if (activeTrack != null) {
            activeTrack!!.pauseOrResume()
        }
    }

    fun clearCurrentTrack() {
        if (activeTrack != null) {
            activeTrack!!.stop()
            activeTrack = null
            activeTrackLevel = 0
        }
        inMeditation = false
        trackCompleted = false
    }

    fun userCompletedTrack() {

    }

    fun userStartedTrack() {

    }

    fun setSettings(settings: SharedPreferences) {
        this.settings = settings
        savedBreathVolume = settings.getFloat("savedBreathVolume", 0.5f)
        this.user.savedBreathVolume = savedBreathVolume
        savedBreathSpeed = settings.getFloat("savedBreathSpeed", 0.5f)
        this.user.savedBreathSpeed = savedBreathSpeed
        savedVoiceVolume = settings.getFloat("savedVoiceVolume", 0.5f)
        this.user.savedVoiceVolume = savedVoiceVolume
        savedMusicVolume = settings.getFloat("savedMusicVolume", 0.5f)
        this.user.savedMusicVolume = savedMusicVolume

        val totalSecondsInMeditation = settings.getInt("totalSecondsInMeditation", 0)
        this.user.totalSecondsInMeditation = totalSecondsInMeditation
    }

    fun incrementTotalSecondsInMeditation() {
        this.user.totalSecondsInMeditation = this.user.totalSecondsInMeditation + 1
        val editor = settings!!.edit()
        editor.putInt("totalSecondsInMeditation", this.user.totalSecondsInMeditation)
        editor.apply()
    }

    fun setDelegate(delegate: TrackDelegate) {
        if (this.activeTrack != null) {
            this.activeTrack!!.delegate = delegate
        }
    }

    fun setBreathVolume(vol: Float) {
        activeTrack!!.setBreathMediaPlayerVolume(vol)
        savedBreathVolume = vol
        this.user.savedBreathVolume = vol
        val editor = settings!!.edit()
        editor.putFloat("savedBreathVolume", this.user.savedBreathVolume)
        editor.apply()
    }

    fun setBreathSpeed(speed: Float) {
        activeTrack!!.setBreathSpeed(speed)
        savedBreathSpeed = speed
        this.user.savedBreathSpeed = speed
        val editor = settings!!.edit()
        editor.putFloat("savedBreathSpeed", this.user.savedBreathSpeed)
        editor.apply()
    }

    fun setVoiceVolume(vol: Float) {
        activeTrack!!.setVoiceMediaPlayerVolume(vol)
        savedVoiceVolume = vol
        this.user.savedVoiceVolume = vol
        val editor = settings!!.edit()
        editor.putFloat("savedVoiceVolume", this.user.savedVoiceVolume)
        editor.apply()
    }

    fun setMusicVolume(vol: Float) {
        activeTrack!!.setMusicMediaPlayerVolume(vol)
        savedMusicVolume = vol
        this.user.savedMusicVolume = vol
        val editor = settings!!.edit()
        editor.putFloat("savedMusicVolume", this.user.savedMusicVolume)
        editor.apply()
    }

    companion object {
        var singleton = BreathworkManager()
    }
}
