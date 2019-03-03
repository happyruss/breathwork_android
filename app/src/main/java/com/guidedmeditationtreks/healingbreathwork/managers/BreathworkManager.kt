package com.guidedmeditationtreks.healingbreathwork.managers

import android.content.Context
import com.guidedmeditationtreks.healingbreathwork.model.Track

class BreathworkManager {
    companion object {
        val singleton = BreathworkManager()
    }
    var activeTrack: Track? = null
    private val trackTemplateFactory = TrackTemplateFactory.singleton

    fun initTrack(activeTrackNumber: Int, context: Context) {
        activeTrack = Track(trackTemplateFactory.trackTemplates[activeTrackNumber], context)
    }


}