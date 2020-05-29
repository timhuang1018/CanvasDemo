package com.timhuang.canvasdemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

const val STROKE_WIDTH = 12f

class MyCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var extraCanvas : Canvas
    private lateinit var extraBitmap : Bitmap

    private val backgroundColor = ResourcesCompat.getColor(resources,R.color.colorBackground,null)
    private val drawColor = ResourcesCompat.getColor(resources,R.color.colorPaint,null)

    private val paint = Paint().apply {
        color = drawColor
        //smooth out edge
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    private var path = Path()

    private var motiontTouchX = 0f
    private var motionTounchY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private lateinit var frame : Rect

    private val drawing = Path()

    private val curPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        val inset = 40
        frame = Rect(inset,inset,width-inset,height-inset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawBitmap(extraBitmap,0f,0f,null)
        canvas.drawPath(drawing,paint)

        canvas.drawPath(curPath,paint)

        canvas.drawRect(frame,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return super.onTouchEvent(event)
        motiontTouchX = event.x
        motionTounchY = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN->touchStart()
            MotionEvent.ACTION_MOVE->touchMove()
            MotionEvent.ACTION_UP->touchUp()
        }
        return true
    }

    private fun touchUp() {

        drawing.addPath(curPath)
        path.reset()
    }

    private fun touchMove() {
        val dx = Math.abs(motiontTouchX -currentX)
        val dy = Math.abs(motionTounchY -currentY)
        if (dx>= touchTolerance || dy>= touchTolerance){
            path.quadTo(currentX,currentY,(motiontTouchX+currentX)/2,(motionTounchY+currentY)/2)
            currentX = motiontTouchX
            currentY = motionTounchY
//            extraCanvas.drawPath(path,paint)
        }
        invalidate()
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motiontTouchX,motionTounchY)
        currentX = motiontTouchX
        currentY = motionTounchY
    }
}