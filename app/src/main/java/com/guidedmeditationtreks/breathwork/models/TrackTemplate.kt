package com.guidedmeditationtreks.breathwork.models

class TrackTemplate(
    trackName: String,
    trackVoiceResourceId: Int,
    trackMusicResourceId: Int? = null,
    secondsWhenBreathStarts: Int? = null,
    secondsWhenBreathStops: Int? = null
) {
    val name = trackName
    val voiceResourceId = trackVoiceResourceId
    val musicResourceId = trackMusicResourceId
    val breathStartSeconds = secondsWhenBreathStarts
    val breathStopSeconds = secondsWhenBreathStops

    var buttonId: Int = 0
    var spacerId: Int = 0

}