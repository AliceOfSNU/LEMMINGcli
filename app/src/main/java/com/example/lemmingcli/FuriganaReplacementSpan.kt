package com.example.lemmingcli

import android.R
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log
import androidx.core.content.ContextCompat
import kotlin.math.ceil


class FuriganaReplacementSpan(val furigana: String, val color: Int): ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        // adjust fm to make space for furigana
//        if (fm != null) {
//            if (-fm.top + fm.ascent < 8) {
//                Log.d("FURIGANA", "fm.top ${fm.top} ascent ${fm.ascent}")
//                // if there is not enough room, "raise" the top
//                fm.top = fm.ascent - 8;
//            }
//        }
        // return text with
        val textWidth = ceil(paint.measureText(furigana)).toInt()
        val origWidth = ceil(paint.measureText(text, start, end)).toInt()
        return Math.max(origWidth, textWidth)
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        // draw the replaced text first
        // it's a [replacement] span!
        //paint.textAlign = Paint.Align.CENTER
        if (text != null) {
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
        val origWidth = ceil(paint.measureText(text, start, end)).toInt()
        var offset = 0
        val font_asc = paint.fontMetricsInt.ascent

        paint.setColor(color)
        // text horizontal size
        paint.textSize = paint.textSize/2
        val ganaWidth = ceil(paint.measureText(furigana)).toInt()
        if(ganaWidth < origWidth){
            offset = (origWidth - ganaWidth)/2
        }

        //paint.textAlign = Paint.Align.CENTER
        Log.d("FURIGANA", paint.fontMetricsInt.toString())
        canvas.drawText(furigana, x + offset, y.toFloat()+font_asc+paint.textSize/2, paint)
    }
}