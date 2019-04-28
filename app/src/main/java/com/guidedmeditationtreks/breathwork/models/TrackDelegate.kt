package com.guidedmeditationtreks.breathwork.models

/**
 * Created by aerozero on 12/23/17.
 */

interface TrackDelegate {
    fun trackTimeRemainingUpdated(timeRemaining: Int)
    fun trackEnded()
}
