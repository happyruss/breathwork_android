package com.guidedmeditationtreks.breathwork.models

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer

/**
 * Created by aerozero on 12/22/17.
 */

class Track(private val trackTemplate: TrackTemplate, context: Context) {

    private var remainingTime: Int = 0
    private var isPaused: Boolean = false
    private var timer: CountDownTimer? = null

    private val playerPart1: MediaPlayer
    private var gapDuration: Int = 0

    private val part1Duration: Int
    private var part2Duration: Int = 0
    var minimumDuration: Int = 0
        private set
    private var totalDuration: Int = 0

    private var playerPart2: MediaPlayer? = null

    private var delegate: TrackDelegate? = null

    val isMultiPart: Boolean
        get() = this.trackTemplate.isMultiPart

    val name: String
        get() = trackTemplate.name

    val longName: String
        get() = trackTemplate.longName


    init {

        //initialize the audio files
        this.playerPart1 = MediaPlayer.create(context, trackTemplate.part1Resource)
        //playerPart1.start(); // no need to call prepare(); create() does that for you

        this.part1Duration = this.playerPart1.duration / 1000

        if (this.trackTemplate.isMultiPart) {
            this.playerPart2 = MediaPlayer.create(context, trackTemplate.part2Resource)
            this.part2Duration = this.playerPart2!!.duration / 1000
            this.minimumDuration = this.part1Duration + this.part2Duration
        } else {
            this.minimumDuration = this.part1Duration
        }
        this.totalDuration = this.minimumDuration
        this.remainingTime = this.totalDuration
    }

    fun setGapDuration(gapDuration: Int) {
        this.gapDuration = gapDuration
        if (this.trackTemplate.isMultiPart) {
            this.totalDuration = this.gapDuration + this.part1Duration + this.part2Duration
        } else {
            this.totalDuration = this.part1Duration
        }
        this.remainingTime = this.totalDuration
    }

    private fun createTimer(seconds: Int) {

        val milliseconds = seconds * 1000
        timer = object : CountDownTimer(milliseconds.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime--
                delegate!!.trackTimeRemainingUpdated(remainingTime)

                if (totalDuration - remainingTime < part1Duration) {
                    if (!playerPart1.isPlaying) {
                        playerPart1.start()
                    }
                }

                if (remainingTime > 0 && trackTemplate.isMultiPart) {
                    if (remainingTime < part2Duration) {
                        if (!playerPart2!!.isPlaying) {
                            playerPart2!!.start()
                        }
                    }
                }
            }

            override fun onFinish() {
                delegate!!.trackTimeRemainingUpdated(0)
                delegate!!.trackEnded()
            }

        }.start()
    }

    fun playFromBeginning() {
        this.createTimer(this.totalDuration)
        this.isPaused = false
        this.playerPart1.start()
    }

    private fun pause() {
        timer!!.cancel()
        this.isPaused = true
        if (this.playerPart1.isPlaying) {
            this.playerPart1.pause()
        }
        if (this.trackTemplate.isMultiPart) {
            if (this.playerPart2!!.isPlaying) {
                this.playerPart2!!.pause()
            }
        }
    }

    private fun resume() {
        this.createTimer(this.remainingTime)
        this.isPaused = false
    }

    fun stop() {
        //        this.remainingTime = this.totalDuration;
        //        this.isPaused = false;
        if (playerPart1.isPlaying) {
            this.playerPart1.stop()
        }
        if (this.trackTemplate.isMultiPart) {
            if (playerPart2!!.isPlaying) {
                this.playerPart2!!.stop()
            }
        }
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    fun pauseOrResume() {
        if (this.isPaused) {
            this.resume()
        } else {
            this.pause()
        }
    }

    fun setDelegate(delegate: TrackDelegate) {
        this.delegate = delegate
    }

}
