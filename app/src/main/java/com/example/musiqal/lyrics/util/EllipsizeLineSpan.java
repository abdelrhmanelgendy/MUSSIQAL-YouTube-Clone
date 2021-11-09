package com.example.musiqal.lyrics.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.LineBackgroundSpan;
import android.text.style.ReplacementSpan;

public class EllipsizeLineSpan extends ReplacementSpan implements LineBackgroundSpan {
    int layoutLeft = 0;
    int layoutRight = 0;

    public EllipsizeLineSpan () {
    }

    @Override
    public void drawBackground (Canvas c, Paint p,
                                int left, int right,
                                int top, int baseline, int bottom,
                                CharSequence text, int start, int end,
                                int lnum) {
        Rect clipRect = new Rect();
        c.getClipBounds(clipRect);
        layoutLeft = clipRect.left;
        layoutRight = clipRect.right;
    }

    @Override
    public int getSize (Paint paint, CharSequence text, int start, int end,
                        Paint.FontMetricsInt fm) {
        return layoutRight - layoutLeft;
    }

    @Override
    public void draw (Canvas canvas, CharSequence text, int start, int end,
                      float x, int top, int y, int bottom, Paint paint) {
        float textWidth = paint.measureText(text, start, end);

        if (x + (int) Math.ceil(textWidth) < layoutRight) {  //text fits
            canvas.drawText(text, start, end, x, y, paint);
        } else {
            float ellipsiswid = paint.measureText("\u2026");
            // move 'end' to the ellipsis point
            end = start + paint.breakText(text, start, end, true, layoutRight - x - ellipsiswid, null);
            canvas.drawText(text, start, end, x, y, paint);
            canvas.drawText("\u2026", x + paint.measureText(text, start, end), y, paint);

        }

    }


}