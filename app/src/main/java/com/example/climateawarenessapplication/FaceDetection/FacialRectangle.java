package com.example.climateawarenessapplication.FaceDetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class FacialRectangle extends GraphicOverlay.Graphic {

    private int RECTANGLE_COLOR = Color.GREEN;
    private float STROKE = 4.0f;
    private Paint rectanglePaint;

    private GraphicOverlay graphicOverlay;
    private Rect rect;


    public FacialRectangle(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        rectanglePaint = new Paint();
        rectanglePaint.setColor(RECTANGLE_COLOR);
        rectanglePaint.setStyle(Paint.Style.STROKE);
        rectanglePaint.setStrokeWidth(STROKE);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateY(rectF.top);
        rectF.bottom = translateY(rectF.bottom);

        canvas.drawRect(rectF,rectanglePaint);
    }
}
