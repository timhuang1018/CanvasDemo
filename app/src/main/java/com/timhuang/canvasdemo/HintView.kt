package com.timhuang.canvasdemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat

class HintView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var recX:Float = 0f
    private var recY:Float = 0f
    private var recW:Int = 0
    private var recH:Int = 60

    private var isInit = false

    private val backgroundColor = ResourcesCompat.getColor(resources,R.color.black_opacity_50,null)
    //    private val paint =
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        textSize = 22f * textFitScreen()
        color = ResourcesCompat.getColor(resources,R.color.colorPaint,null)
    }

    val rect = Rect(50,50,1000,1000)
    lateinit var pTopLeft :PointF
    lateinit var pBotRight : PointF
    lateinit var hole :RectF

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!isInit){
            isInit = true
//            recX = (w/2).toFloat()
//            recY = (h/2).toFloat()
            recW = w
            pTopLeft = PointF(recX,recY)
            pBotRight = PointF(recX+recW,recY+recH)
            hole = RectF(pTopLeft.x,pTopLeft.y,pBotRight.x,pBotRight.y)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d("hintView","onDraw called")
        super.onDraw(canvas)


        if (canvas==null) return
//        canvas.drawRect(rect,paint)
//        canvas.drawColor(backgroundColor)
        canvas.clipRect(hole,Region.Op.DIFFERENCE)
//        canvas.drawARGB(50,255,0,0)
        canvas.drawColor(backgroundColor)
        canvas.clipRect(Rect(0,0,canvas.width,canvas.height))

        canvas.drawText("hello",recX,recY-200,paint)
    }

    fun setRetangle(x:Float,y:Float,width:Int,height:Int){
        Log.d("hintView","setRetangle called,$x,$y,$width,$height")

        recX = x
        recY = y
        recW = width
        recH = height
        invalidate()
    }

    fun textFitScreen(): Float {
        return resources.displayMetrics.density
    }
}