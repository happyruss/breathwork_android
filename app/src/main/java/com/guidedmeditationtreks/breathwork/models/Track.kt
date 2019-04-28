package com.guidedmeditationtreks.breathwork.models

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.CountDownTimer
import com.guidedmeditationtreks.breathwork.R

class Track(trackTemplate: TrackTemplate, context: Context, musicVolumeInit: Float, breathVolumeInit: Float, voiceVolumeInit: Float, breathSpeedInit: Float) {

    private var voiceMediaPlayer: MediaPlayer = MediaPlayer.create(context, trackTemplate.voiceResourceId)
    private var musicMediaPlayer: MediaPlayer? = null
    private var remainingTime: Int = 0
    private var isPaused: Boolean = false
    private var timer: CountDownTimer? = null
    private var trackTemplateProperty = trackTemplate
    private val soundPool: SoundPool
    private val breathSoundId: Int
    private var breathStreamId: Int? = null
    private var musicVolume: Float = musicVolumeInit
    private var breathVolume: Float = breathVolumeInit
    private var voiceVolume: Float = voiceVolumeInit
    private var breathIsPlaying: Boolean = false
    private var breathSpeed: Float = breathSpeedInit

    var delegate: TrackDelegate? = null
    val name = trackTemplate.name
    val duration: Int

    init {
        duration = voiceMediaPlayer.duration
        remainingTime = duration / 1000
        if (trackTemplate.musicResourceId != null) {
            musicMediaPlayer = MediaPlayer.create(context, trackTemplate.musicResourceId)
        }
        val attributes = AudioAttributes.Builder().apply {
            setUsage(AudioAttributes.USAGE_GAME)
            setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        }.build()

        soundPool = SoundPool.Builder().apply {
            setMaxStreams(1)
            setAudioAttributes(attributes)
        }.build()

        breathSoundId = soundPool.load(context, R.raw.breathloop, 3)
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
            if (breathStreamId != null) {
                soundPool.pause(breathStreamId as Int)
            }
        }
    }

    fun stop() {
        if (voiceMediaPlayer.isPlaying) {
            voiceMediaPlayer.stop()
        }
        if (musicMediaPlayer != null && (musicMediaPlayer as MediaPlayer).isPlaying) {
            (musicMediaPlayer as MediaPlayer).stop()
            if (breathStreamId != null) {
                soundPool.stop(breathStreamId as Int)
            }
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
        this.voiceVolume = vol
        this.voiceMediaPlayer.setVolume(vol, vol)
    }
    fun setMusicMediaPlayerVolume(vol: Float) {
        this.musicVolume = vol
        if (musicMediaPlayer != null) {
            (musicMediaPlayer as MediaPlayer).setVolume(vol, vol)
        }
    }
    fun setBreathMediaPlayerVolume(vol: Float) {
        this.breathVolume = vol
        if (breathStreamId != null) {
            soundPool.setVolume(breathStreamId as Int, vol, vol)
        }
    }

    fun setBreathSpeed(speed: Float) {
        breathSpeed = speed
        if (breathStreamId != null) {
            soundPool.stop(breathStreamId as Int)
            breathStreamId = soundPool.play(breathSoundId, breathVolume, breathVolume, 1, -1, breathSpeed)
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
                    if (trackTemplateProperty.breathStartSeconds !== null && trackTemplateProperty.breathStopSeconds !== null) {
                        val breathStartSeconds = (trackTemplateProperty.breathStartSeconds as Int)
                        val breathStopSeconds = (trackTemplateProperty.breathStopSeconds as Int)
                        val currentPosition = voiceMediaPlayer.currentPosition
                        val breathShouldBePlaying = (currentPosition >= breathStartSeconds) && (currentPosition <= breathStopSeconds)
                        if (breathShouldBePlaying) {
                            if (!breathIsPlaying) {
                                breathStreamId = soundPool.play(breathSoundId, breathVolume, breathVolume, 1, -1, breathSpeed)
                                breathIsPlaying = true
                            }
                        } else {
                            if (breathIsPlaying && breathStreamId != null) {
                                soundPool.stop(breathStreamId as Int)
                                breathIsPlaying = false
                            }
                        }
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