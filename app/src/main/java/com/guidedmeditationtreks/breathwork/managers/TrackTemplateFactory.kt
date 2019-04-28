package com.guidedmeditationtreks.breathwork.managers

import com.guidedmeditationtreks.breathwork.R
import com.guidedmeditationtreks.breathwork.models.TrackTemplate

import java.util.jar.Attributes

/**
 * Created by aerozero on 12/22/17.
 */

class TrackTemplateFactory {

    private val trackTemplates: Array<TrackTemplate> = arrayOf(
            TrackTemplate("Step Into Your Power", "Silent Meditation", R.raw.m01music, R.raw.m01voice)
            , TrackTemplate("Step Into Your Power", "Silent Meditation", R.raw.m01music, R.raw.m01voice)
            , TrackTemplate("Step Into Your Power", "Silent Meditation", R.raw.m01music, R.raw.m01voice)
            , TrackTemplate("Step Into Your Power", "Silent Meditation", R.raw.m01music, R.raw.m01voice)
            , TrackTemplate("Step Into Your Power", "Silent Meditation", R.raw.m01music, R.raw.m01voice)
    )

    val requireMeditationsBeDoneInOrder = true
    val trackTemplateCount: Int
        get() = trackTemplates.size

    init {

    }

    fun getTrackTemplate(index: Int): TrackTemplate {
        return trackTemplates[index]
    }

    companion object {

        var singleton = TrackTemplateFactory()
    }
}
