package com.ci.marcopolo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class Drawer extends View {
    public final static String TAG = "Drawer";

    private int actionBarHeight;

    // drawing objects
    private Paint paint;
    private Bitmap canvasOverlayBitmap;
    private Canvas canvasOverlay;
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
        // setup paint
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        // setup canvasOverlay
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inScaled = true;
        Bitmap existingBitmap = BitmapFactory.decodeFile(Constants.AUTOPOLO_IMAGE_FILENAME, options);
        canvasOverlayBitmap = existingBitmap.copy(existingBitmap.getConfig(), true);
        canvasOverlay = new Canvas(canvasOverlayBitmap);
        // setup lineList
        setLineList(new ArrayList<ArrayList<PointF>>());
        getLineList().add(new ArrayList<PointF>());  // create an initial line to fill
        setColor(Color.WHITE);

        // get action bar height
        try {
            final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                    new int[]{android.R.attr.actionBarSize});
            actionBarHeight = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Action bar error wtf", Toast.LENGTH_SHORT).show();
            actionBarHeight = 100;  // a guess lol
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOnCanvas(canvas);
        drawOnCanvas(canvasOverlay);
    }

    private void drawOnCanvas(Canvas canvas) {
        for (int i = 0; i < getLineList().size(); i++) {
            ArrayList<PointF> currentLine = getLineList().get(i);
            if (currentLine.size() == 0) {
                continue;
            } else if (currentLine.size() == 1) {
                PointF point = currentLine.get(0);
                canvas.drawPoint(point.x, point.y, paint);
            } else {
                PointF point = currentLine.get(0);
                canvas.drawPoint(point.x, point.y, paint);
                drawLine(canvas, currentLine);
            }
        }
    }

    private void drawLine(Canvas canvas, ArrayList<PointF> line) {
        for (int j = 0; j < line.size() - 1; j++) {
            PointF currentPoint = line.get(j);
            PointF nextPoint = line.get(j + 1);

            canvas.drawLine(currentPoint.x,
                    currentPoint.y,
                    nextPoint.x,
                    nextPoint.y,
                    paint);
        }
    }

    private void addPoint(PointF point) {
        getLineList().get(getLineList().size() - 1).add(point);
    }

    private void createNewLine() {
        getLineList().add(new ArrayList<PointF>());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        PointF currentPoint = new PointF(event.getRawX(), event.getRawY() - actionBarHeight / 2);
        //Log.d(TAG, "X: " + event.getRawX());
        //Log.d(TAG, "Y: " + event.getRawY());
        if (action == MotionEvent.ACTION_DOWN) {
            addPoint(currentPoint);

            invalidate();
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            addPoint(currentPoint);

            invalidate();
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (getLineList().get(getLineList().size() - 1).size() != 1) {
                addPoint(currentPoint);
            }
            createNewLine();

            invalidate();
            return true;
        } else {
            invalidate();
        }
        return true;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public ArrayList<ArrayList<PointF>> getLineList() {
        return lineList;
    }

    public void setLineList(ArrayList<ArrayList<PointF>> lineList) {
        this.lineList = lineList;
    }

    public Canvas getCanvasOverlay() {
        return canvasOverlay;
    }

    public Bitmap getCanvasOverlayBitmap() {
        return canvasOverlayBitmap;
    }
}
