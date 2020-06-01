package com.timhuang.canvasdemo

import android.graphics.Rect
import android.graphics.RectF
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
//        hintView.setRetangle(0f,(getScreenHeight()/2).toFloat(),width,200)
        hintView.setHint(
            Hint(
                Rect(100,(getScreenHeight()/2),500,(getScreenHeight()/2)+200),"一般貼文：會在動態牆出現，所有人都可以免費瀏覽。"
            )
        )

        hintView.setHint(
            Hint(
                //44
                Rect(600,(getScreenHeight()/2),1000,(getScreenHeight()/2)+200),"訂閱貼文：限定有付費訂閱你的用戶才能收看，此功能必須聯絡你的經紀人討論定價後才能開啟收費。"
            )
        )


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
