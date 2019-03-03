package com.guidedmeditationtreks.healingbreathwork.model

interface TrackDelegate {
    fun trackTimeRemainingUpdated(timeRemaining: Int)
    fun trackEnded()
}
