package com.example.stakemodual.utils

import android.app.Activity
import android.speech.tts.TextToSpeech
import java.util.Locale

class AudioListener(activity: Activity) {

    private val mTTS by lazy {
        TextToSpeech(activity) { res ->
            if (res != TextToSpeech.ERROR) {
                speak()
            }
        }
    }

    private fun speak() {
        mTTS.language = Locale.ENGLISH
        mTTS.setPitch(0.7f)
        mTTS.setSpeechRate(0.7f)
    }

    fun speak(data: String) {
        mTTS.speak(data, TextToSpeech.QUEUE_FLUSH, null,null)
    }


}