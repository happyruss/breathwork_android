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
    private val user: User
    private var activeTrack: Track? = null
    private var activeTrackLevel: Int = 0
    var inMeditation: Boolean? = null
    var trackCompleted: Boolean? = null

    val isMultiPart: Boolean
        get() = activeTrack!!.isMultiPart

    var defaultDurationMinutes: Int
        get() = user.customMeditationDurationMinutes
        set(durationMinutes) {
            val editor = settings!!.edit()
            editor.putInt("savedCustomMeditationDurationMinutes", durationMinutes)
            editor.apply()
            this.user.customMeditationDurationMinutes = durationMinutes
        }

    val minimumDuration: Int
        get() = activeTrack!!.minimumDuration

    val userCompletedTrackLevel: Int
        get() = user.completedTrackLevel

    val userTotalSecondsInMeditation: Int
        get() = user.totalSecondsInMeditation

    val activeTrackName: String?
        get() = if (activeTrack == null) {
            null
        } else activeTrack!!.name

    val activeTrackLongName: String?
        get() = if (activeTrack == null) {
            null
        } else activeTrack!!.longName

    init {
        this.user = User()
    }

    fun initTrackAtLevel(trackLevel: Int, context: Context) {
        this.clearCurrentTrack()
        this.activeTrackLevel = trackLevel
        val trackTemplate = trackTemplateFactory.getTrackTemplate(trackLevel)
        this.activeTrack = Track(trackTemplate, context)
    }

    fun playActiveTrackFromBeginning(gapDuration: Int) {
        this.activeTrack!!.setGapDuration(gapDuration)
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

    private fun setCompletedTrack() {
        if (this.activeTrackLevel > this.user.completedTrackLevel) {
            this.user.completedTrackLevel = this.activeTrackLevel
            val editor = settings!!.edit()
            editor.putInt("savedCompletedLevel", this.activeTrackLevel)
            editor.apply()
        }
    }

    fun userCompletedTrack() {
        this.setCompletedTrack()
    }

    fun userStartedTrack() {
        this.setCompletedTrack()
    }

    fun setSettings(settings: SharedPreferences) {
        this.settings = settings
        val savedCompletedLevel = settings.getInt("savedCompletedLevel", 0)
        this.user.completedTrackLevel = savedCompletedLevel

        val savedCustomMeditationDurationMinutes = settings.getInt("savedCustomMeditationDurationMinutes", 0)
        this.user.customMeditationDurationMinutes = savedCustomMeditationDurationMinutes
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
            this.activeTrack!!.setDelegate(delegate)
        }
    }

    companion object {

        var singleton = BreathworkManager()
    }
}
