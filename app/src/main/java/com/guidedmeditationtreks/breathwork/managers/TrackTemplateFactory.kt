package com.guidedmeditationtreks.breathwork.managers

import com.guidedmeditationtreks.breathwork.R
import com.guidedmeditationtreks.breathwork.models.TrackTemplate

class TrackTemplateFactory {
    companion object {
        val singleton = TrackTemplateFactory()
    }

    val trackTemplates: Array<TrackTemplate>

    val trackTemplateCount: Int
        get() = trackTemplates.size

    fun getTrackTemplate(index: Int): TrackTemplate {
        return trackTemplates[index]
    }

    init {
        val trackTemplateIntro = TrackTemplate("Introduction", R.raw.introduction)
        val trackTemplates0 = TrackTemplate("Stepping Into Your Power", R.raw.m01voice, R.raw.m01music, 99, 1396)
        val trackTemplates1 = TrackTemplate("Grieving and Celebrating a Loss", R.raw.m02voice, R.raw.m02music, 143, 1428)
        val trackTemplates2 = TrackTemplate("Healing Sexual Abuse", R.raw.m03voice, R.raw.m03music, 167, 1620)
        val trackTemplates3 = TrackTemplate("Abundance", R.raw.m04voice, R.raw.m04music, 103, 1652)

        trackTemplates =  arrayOf(trackTemplateIntro, trackTemplates0, trackTemplates1, trackTemplates2, trackTemplates3)
    }
}