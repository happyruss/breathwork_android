package com.guidedmeditationtreks.breathwork.models

import android.content.Context
import android.media.MediaPlayer

import com.guidedmeditationtreks.breathwork.R

import java.io.File

/**
 * Created by aerozero on 12/21/17.
 */

class TrackTemplate(val name: String, val longName: String, val part1Resource: Int, part2Resource: Int) {
    var isMultiPart: Boolean = false
        private set
    var part2Resource: Int = 0
        private set
    var buttonId: Int = 0
    var spacerId: Int = 0

    init {

        try {
            if (part2Resource != 0) {
                this.part2Resource = part2Resource
                this.isMultiPart = true
            } else {
                this.isMultiPart = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
