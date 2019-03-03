package com.guidedmeditationtreks.healingbreathwork.managers

import com.guidedmeditationtreks.healingbreathwork.R
import com.guidedmeditationtreks.healingbreathwork.model.TrackTemplate

class TrackTemplateFactory {
    companion object {
        val singleton = TrackTemplateFactory()
    }

    val trackTemplates: Array<TrackTemplate>
    init {
        val trackTemplateIntro = TrackTemplate("Introduction", R.raw.all00)
        val trackTemplates0 = TrackTemplate("Stepping Into Your Power", R.raw.voice01, R.raw.music01, R.raw.breathloop, 99, 1396)
        val trackTemplates1 = TrackTemplate("Grieving and Celebrating a Loss", R.raw.voice02, R.raw.music02, R.raw.breathloop, 143, 1428)
        val trackTemplates2 = TrackTemplate("Healing Sexual Abuse", R.raw.voice03, R.raw.music03, R.raw.breathloop, 167, 1620)
        val trackTemplates3 = TrackTemplate("Abundance", R.raw.voice04, R.raw.music04, R.raw.breathloop, 103, 1652)

        trackTemplates =  arrayOf(trackTemplateIntro, trackTemplates0, trackTemplates1, trackTemplates2, trackTemplates3)
    }
}