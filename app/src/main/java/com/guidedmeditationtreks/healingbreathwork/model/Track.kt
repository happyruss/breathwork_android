package com.guidedmeditationtreks.healingbreathwork.model

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer

class Track(trackTemplate: TrackTemplate, context: Context) {

    private var voiceMediaPlayer: MediaPlayer = MediaPlayer.create(context, trackTemplate.voiceResourceId)
    private var musicMediaPlayer: MediaPlayer? = null
    private var breathMediaPlayer: MediaPlayer? = null
    private var remainingTime: Int = 0
    private var isPaused: Boolean = false
    private var timer: CountDownTimer? = null
    private var trackTemplateProperty = trackTemplate

    var delegate: TrackDelegate? = null
    val name = trackTemplate.name
    val duration: Int

    init {
        duration = voiceMediaPlayer.duration
        if (trackTemplate.musicResourceId != null) {
            musicMediaPlayer = MediaPlayer.create(context, trackTemplate.musicResourceId)
        }
        if (trackTemplate.breathResourceId != null) {
            breathMediaPlayer = MediaPlayer.create(context, trackTemplate.breathResourceId)
            (breathMediaPlayer as MediaPlayer).isLooping = true
        }
    }

    fun playFromBeginning() {
        this.createTimer(this.duration)
        this.isPaused = false
    }


    fun resume() {
        this.createTimer(this.remainingTime)
        this.isPaused = false
    }

    fun pause() {
        (timer as CountDownTimer).cancel()
        this.isPaused = true
        if (voiceMediaPlayer.isPlaying) {
            voiceMediaPlayer.pause()
        }
        if (musicMediaPlayer != null && (musicMediaPlayer as MediaPlayer).isPlaying) {
            (musicMediaPlayer as MediaPlayer).pause()
        }
        if (breathMediaPlayer != null && (breathMediaPlayer as MediaPlayer).isPlaying) {
            (breathMediaPlayer as MediaPlayer).pause()
        }
    }

    fun stop() {
        if (voiceMediaPlayer.isPlaying) {
            voiceMediaPlayer.stop()
        }
        if (musicMediaPlayer != null && (musicMediaPlayer as MediaPlayer).isPlaying) {
            (musicMediaPlayer as MediaPlayer).stop()
        }
        if (breathMediaPlayer != null && (breathMediaPlayer as MediaPlayer).isPlaying) {
            (breathMediaPlayer as MediaPlayer).stop()
        }
        if (timer != null) {
            (timer as CountDownTimer).cancel()
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


    fun setVoiceMediaPlayerVolume(vol: Float) {
        this.voiceMediaPlayer.setVolume(vol, vol)
    }
    fun setMusicMediaPlayerVolume(vol: Float) {
        if (musicMediaPlayer != null) {
            (musicMediaPlayer as MediaPlayer).setVolume(vol, vol)
        }
    }
    fun setBreathMediaPlayerVolume(vol: Float) {
        if (breathMediaPlayer != null) {
            (breathMediaPlayer as MediaPlayer).setVolume(vol, vol)
        }
    }

    private fun createTimer(seconds: Int) {

        val milliseconds = seconds * 1000
        timer = object : CountDownTimer(milliseconds.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime--
                if (delegate !== null) {
                    (delegate as TrackDelegate).trackTimeRemainingUpdated(remainingTime)
                }

                if (!voiceMediaPlayer.isPlaying) {
                    voiceMediaPlayer.start()
                }
                if (musicMediaPlayer !== null && !(musicMediaPlayer as MediaPlayer).isPlaying) {
                    (musicMediaPlayer as MediaPlayer).start()
                }
                if (breathMediaPlayer !== null && trackTemplateProperty.breathStartSeconds !== null && trackTemplateProperty.breathStopSeconds !== null) {
                    val breathStartSeconds = (trackTemplateProperty.breathStartSeconds as Int)
                    val breathStopSeconds = (trackTemplateProperty.breathStopSeconds as Int)
                    val currentPosition = voiceMediaPlayer.currentPosition
                    val breathShouldBePlaying = (currentPosition >= breathStartSeconds) && (currentPosition <= breathStopSeconds)
                    if (breathShouldBePlaying) {
                        (breathMediaPlayer as MediaPlayer).start()
                    }
                }
            }

            override fun onFinish() {
                if (delegate !== null) {
                    (delegate as TrackDelegate).trackTimeRemainingUpdated(0)
                    (delegate as TrackDelegate).trackEnded()
                }
            }

        }.start()
    }
}