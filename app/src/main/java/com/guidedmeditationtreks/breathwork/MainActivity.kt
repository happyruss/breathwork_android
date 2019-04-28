package com.guidedmeditationtreks.breathwork

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

import com.guidedmeditationtreks.breathwork.managers.TrackTemplateFactory
import com.guidedmeditationtreks.breathwork.managers.BreathworkManager
import com.guidedmeditationtreks.breathwork.models.TrackTemplate

import java.util.Locale

import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class MainActivity : AppCompatActivity() {
    var breathworkManager = BreathworkManager.singleton
    var trackTemplateFactory = TrackTemplateFactory.singleton

    private var timerButton: ImageButton? = null
    private var infoButton: ImageButton? = null
    private var meditationTotalTimeTextView: TextView? = null

    fun didTapTimerButton(v: View) {
        presentAlerts(0)
    }


    fun didTapMeditationButton(v: View) {
        val trackLevel = v.tag as Int
        breathworkManager.initTrackAtLevel(trackLevel, this)
        val myIntent = Intent(this@MainActivity, MeditationActivity::class.java)
        startActivityForResult(myIntent, MEDITATION_ACTIVITY_REQUEST_CODE)
    }

    fun didTapInfoButton(v: View) {
        val uriUrl = Uri.parse(resources.getString(R.string.url))
        val webView = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(webView)
    }

    private fun secureButtons() {

        val disabledAlpha = 0.5f
        val totalTrackCount = trackTemplateFactory.trackTemplateCount
        timerButton!!.isEnabled = true

        for (i in 1 until totalTrackCount) {
            val trackTemplate = trackTemplateFactory.getTrackTemplate(i)

            val isNotLastTrack = i < totalTrackCount - 1
            val linearLayout = this.findViewById<LinearLayout>(R.id.buttonLinearLayout)

            val button = linearLayout.findViewById<Button>(trackTemplate.buttonId)
            button.isEnabled = true

        }

        val medHours = breathworkManager.userTotalSecondsInMeditation / 3600
        val meditationTimeLabelText = if (medHours == 1) String.format(Locale.getDefault(), "%d hour spent meditating", medHours) else String.format(Locale.getDefault(), "%d hours spent meditating", medHours)
        meditationTotalTimeTextView!!.setText(meditationTimeLabelText)
    }

    private fun connectView() {
        timerButton = findViewById(R.id.silentTimerButton)
        infoButton = findViewById(R.id.infoButton)
        meditationTotalTimeTextView = findViewById(R.id.meditationTotalTimeTextView)

        val trackCount = trackTemplateFactory.trackTemplateCount

        for (i in 0 until trackCount) {
            val trackTemplate = trackTemplateFactory.getTrackTemplate(i)
            trackTemplate.buttonId = View.generateViewId()

            val v = LayoutInflater.from(this).inflate(R.layout.button_template, null)
            val button = v.findViewById<Button>(R.id.templateButton)
            button.tag = i
            button.id = trackTemplate.buttonId
            button.text = trackTemplate.name
            button.setOnClickListener { v -> didTapMeditationButton(v) }
            val linearLayout = this.findViewById<LinearLayout>(R.id.buttonLinearLayout)
            val parent = button.parent as LinearLayout
            parent.removeView(button)
            linearLayout.addView(button)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectView()
        val settings = getSharedPreferences(PREFS_NAME, 0)
        breathworkManager.setSettings(settings)
        this.secureButtons()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/sf-pro-text-semibold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    private fun presentAlerts(trackLevel: Int) {
//        presentCountdownLengthAlert()
    }

//    private fun presentCountdownLengthAlert(trackLevel: Int) {
//
//        val layoutInflater = LayoutInflater.from(this)
//            val promptView = layoutInflater.inflate(R.layout.prompt, null)
//            val alertD = AlertDialog.Builder(this).create()
//
//            val btnCustom = promptView.findViewById<Button>(R.id.btnCustom)
//            val userInput = promptView.findViewById<EditText>(R.id.userInput)
//
//            var customValue = breathworkManager.defaultDurationMinutes
//            if (customValue < minDurationMinutes) {
//                customValue = minDurationMinutes
//            }
//
//            userInput.setText(String.format(Locale.getDefault(), "%d", customValue))
//            userInput.setOnClickListener { userInput.setText("") }
//
//            btnCustom.setOnClickListener(View.OnClickListener {
//                val userValue: Int?
//                try {
//                    userValue = Integer.parseInt(userInput.text.toString())
//                } catch (ex: NumberFormatException) {
//                    presentInvalidCustomCountdownAlert(trackLevel, minDurationMinutes)
//                    return@OnClickListener
//                }
//
//                if (userValue < minDurationMinutes) {
//                    presentInvalidCustomCountdownAlert(trackLevel, minDurationMinutes)
//                } else {
//                    breathworkManager.defaultDurationMinutes = userValue
//                    runMeditationWithFullLength(userValue * 60)
//                    alertD.dismiss()
//                }
//            })
//            alertD.setView(promptView)
//            alertD.show()
//        }
//    }
//
//    private fun presentInvalidCustomCountdownAlert(trackLevel: Int, minDurationMinutes: Int) {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        val minAlertString = String.format(Locale.getDefault(), "Length for this meditation must be at least %d minutes", minDurationMinutes)
//
//        alertDialogBuilder.setTitle("Invalid Custom Time")
//        alertDialogBuilder
//                .setMessage(minAlertString)
//                .setCancelable(false)
//                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == MEDITATION_ACTIVITY_REQUEST_CODE)
//            secureButtons()
//    }
//
//    private fun runMeditationWithGap(gapAmount: Int) {
//        val myIntent = Intent(this@MainActivity, MeditationActivity::class.java)
//        myIntent.putExtra("gapAmount", gapAmount)
//        //        MainActivity.this.startActivity(myIntent);
//        startActivityForResult(myIntent, MEDITATION_ACTIVITY_REQUEST_CODE)
//    }
//
//    private fun runMeditationWithFullLength(fullLengthSeconds: Int) {
//        val minDurationSeconds = breathworkManager.minimumDuration
//        val gapLength = fullLengthSeconds - minDurationSeconds
//        runMeditationWithGap(gapLength)
//    }

    companion object {
        val PREFS_NAME = "BreathworkPrefs"
        private val MEDITATION_ACTIVITY_REQUEST_CODE = 0xe110
    }

}
