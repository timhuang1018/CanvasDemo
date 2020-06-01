package com.timhuang.canvasdemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.*


class HintView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var recW:Int = 0
    private var charW = 0
    private var charH = 0

    private var isInit = false

    private val backgroundColor = ResourcesCompat.getColor(resources,R.color.black_opacity_50,null)
    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        textSize = 22f * textFitScreen()
        color = ResourcesCompat.getColor(resources,R.color.colorPaint,null)
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 3f
        textSize = 18f * textFitScreen()
        typeface = Typeface.SERIF
        color = ResourcesCompat.getColor(resources,R.color.colorPaint,null)
    }
    private val hints = mutableListOf<Hint>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!isInit){
            isInit = true
            recW = w
            val rect = Rect()
            textPaint.getTextBounds("國字",0,2,rect)
            charW = abs(rect.right-rect.left)/2
            charH = abs(rect.bottom-rect.top) + (3 * textFitScreen()).toInt()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d("hintView","onDraw called")
        super.onDraw(canvas)

        if (canvas==null) return
        drawAllHints(canvas)
    }

    //second one (index>0) will be on top
    private fun drawAllHints(canvas: Canvas) {
        hints.forEach {hint->
            hint.apply {
                canvas.clipRect(area,Region.Op.DIFFERENCE)
            }
        }
        canvas.drawColor(backgroundColor)
        hints.forEachIndexed { index,hint->
            hint.apply {
                var locationX = area.left + (area.right-area.left)/2.toFloat()
                var locationY = if (index==0) area.bottom.toFloat()+30 else area.top.toFloat()-30

                val rect = Rect()
                textPaint.getTextBounds(description,0,description.length-1,rect)
                val blockWidth = area.right-area.left + 100
                val textWidth = abs(rect.right - rect.left)
                Log.d("drawAllHints","blockWidth:$blockWidth,textWidth $textWidth,charW:$charW")

                val w = 150
                val h = 300

                if (index==0){
                    drawIndicateLine(locationX,locationY,locationX+w,locationY+h,canvas)
                }else{
                    drawIndicateLine(locationX-w,locationY-h,locationX,locationY,canvas)
                }

                //move location to description start point
                if (index==0) {
                    locationX -= w / 2
                    locationY += h + 100
                }else{
                    locationX -= (w + (area.right-area.left)/2)
                    locationY -= (h + 50)
                }

                if (index>0){
                    val lines = textWidth/blockWidth
                    locationY -= lines * charH
                }

                if (textWidth>blockWidth){
                    val oneLine = blockWidth/charW
                    Log.d("drawAllHints","oneLine :$oneLine")
                    val list = description.chunked(oneLine)
                    Log.d("drawAllHints","list :$list")

                    list.forEach {
                        canvas.drawText(it,locationX,locationY,textPaint)
                        locationY += charH
                    }
                }else{
                    canvas.drawText(description,locationX,locationY,textPaint)

                }

            }
        }
    }

    fun setHint(hint: Hint){
        hints.add(hint)
        invalidate()
    }

    fun textFitScreen(): Float {
        return resources.displayMetrics.density
    }

    fun drawIndicateLine(x1: Float, y1: Float, x2: Float, y2: Float,  canvas: Canvas) {
        val diffX = x2 - x1
        val diffY = y2 - y1
        val midX = x1 + (diffX) / 2
        val midY = y1 + (diffY) / 2
        val curveRadius = - min(diffX,diffY)/2
        val ovalDiameter = min(diffX,diffY)/10

        val path = Path()
        drawArcLine(path,x1,y1,midX,midY,curveRadius)
        path.addOval(RectF(midX-ovalDiameter,midY,midX+ovalDiameter,midY-ovalDiameter*2),Path.Direction.CW)
        drawArcLine(path,midX-ovalDiameter,midY-ovalDiameter,x2,y2,curveRadius)
        canvas.drawPath(path, linePaint)
    }

    fun drawArcLine(path:Path,x1: Float, y1: Float, x2: Float, y2: Float, curveRadius: Float){
        val midX = x1 + (x2 - x1) / 2
        val midY = y1 + (y2 - y1) / 2
        val xDiff = midX - x1
        val yDiff = midY - y1
        val angle = atan2(yDiff.toDouble(), xDiff.toDouble()) * (180 / Math.PI) - 90
        val angleRadians = Math.toRadians(angle)
        val pointX =
            (midX + curveRadius * cos(angleRadians)).toFloat()
        val pointY =
            (midY + curveRadius * sin(angleRadians)).toFloat()
        path.moveTo(x1, y1)
        path.cubicTo(x1, y1, pointX, pointY, x2, y2)
    }
}

data class Hint(val area: Rect, var description:String)

