package com.timhuang.canvasdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {

    lateinit var hintView: HintView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val myCanvasView =MyCanvasView(this)
//        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
//        myCanvasView.contentDescription =  getString(R.string.canvasContentDescription)
//        setContentView(myCanvasView)
        hintView = HintView(this)
        setContentView(hintView)

        createHint()
    }

    private fun createHint() {
        val width = getScreenWidht()
        hintView.setRetangle(0f,(getScreenHeight()/2).toFloat(),width,200)
    }

    private fun getScreenWidht(): Int {
        val displayMatric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMatric)
        return displayMatric.widthPixels
    }

    private fun getScreenHeight(): Int {
        val displayMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetric)
        return displayMetric.heightPixels
    }

}
