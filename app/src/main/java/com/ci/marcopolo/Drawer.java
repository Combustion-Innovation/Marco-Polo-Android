package com.ci.marcopolo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class Drawer extends View {
    public final static String TAG = "Drawer";

    // drawing objects
    private Paint paint;
    private int currentLineIndex = 0;
    private ArrayList<ArrayList<PointF>> lineList;

    public Drawer(Context context) {
        super(context);
        init();
    }

    public Drawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Drawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        lineList = new ArrayList<ArrayList<PointF>>();
        lineList.add(new ArrayList<PointF>());  // create an initial line to fill
        setColor(Color.WHITE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < lineList.size(); i++) {
            ArrayList<PointF> currentLine = lineList.get(i);
            if (currentLine.size() < 2) continue;
            for (int j = 0; j < currentLine.size() - 1; j++) {
                PointF currentPoint = currentLine.get(j);
                PointF nextPoint = currentLine.get(j + 1);

                canvas.drawLine(currentPoint.x,
                        currentPoint.y,
                        nextPoint.x,
                        nextPoint.y,
                        paint);
            }
        }
    }

    private void addPoint(PointF point) {
        lineList.get(currentLineIndex).add(point);
    }

    private void createNewLine() {
        currentLineIndex++;
        lineList.add(new ArrayList<PointF>());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            Log.d(TAG, "Adding a point to the current pointList");
            PointF currentPoint = new PointF(event.getRawX(), event.getRawY());
            addPoint(currentPoint);

            invalidate();
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Adding a line to the lineList");
            PointF currentPoint = new PointF(event.getRawX(), event.getRawY());
            addPoint(currentPoint);
            createNewLine();

            invalidate();
            return true;
        }
        return true;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}
