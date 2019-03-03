package com.guidedmeditationtreks.healingbreathwork.model

class TrackTemplate(
    trackName: String,
    trackVoiceResourceId: Int,
    trackMusicResourceId: Int? = null,
    trackBreathResourceId: Int? = null,
    secondsWhenBreathStarts: Int? = null,
    secondsWhenBreathStops: Int? = null
) {
    val name = trackName
    val voiceResourceId = trackVoiceResourceId
    val musicResourceId = trackMusicResourceId
    val breathResourceId = trackBreathResourceId
    val breathStartSeconds = secondsWhenBreathStarts
    val breathStopSeconds = secondsWhenBreathStops
}