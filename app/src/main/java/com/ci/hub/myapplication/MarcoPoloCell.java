package com.ci.hub.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by Alex on 1/29/15.
 */
public class MarcoPoloCell extends RelativeLayout {
    public final static String TAG = "MarcoPoloCell";
    public final static int MARCO = 0;
    public final static int MARCO_DARK = 1;
    public final static int POLO = 2;
    public final static int POLO_DARK = 3;
    public final static int FRIEND = 4;
    public final static int FRIEND_DARK = 5;
    public final static int CONTACT = 6;
    public final static int CONTACT_DARK = 7;

    private int textColor;
    private int cellType;

    private Button center;
    private TextView topLeft;
    private TextView topRight;
    private TextView bottomLeft;
    private TextView bottomRight;

    public MarcoPoloCell(Context context) {
        super(context);
        init(context);
    }

    public MarcoPoloCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.MarcoPoloCell,
            0, 0
        );

        init(context, a);
    }

    private void init(Context context) {
        // create the MarcoPoloCell
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.old_layout_marcopolo_cell, this);

        center = (Button) findViewById(R.id.marcopolo_cell_center);
        topLeft = (TextView) findViewById(R.id.marcopolo_cell_top_left);
        topRight = (TextView) findViewById(R.id.marcopolo_cell_top_right);
        bottomLeft = (TextView) findViewById(R.id.marcopolo_cell_bottom_left);
        bottomRight = (TextView) findViewById(R.id.marcopolo_cell_bottom_right);
    }

    private void init(Context context, TypedArray a) {
        init(context);

        // set the attributes
        try {
            setTextColor(a.getInt(R.styleable.MarcoPoloCell_textColor, getResources().getColor(R.color.white)));
            setCellType(a.getInt(R.styleable.MarcoPoloCell_cellType, MARCO));

            setCenterText(a.getString(R.styleable.MarcoPoloCell_centerText));
            setTopLeftText(a.getString(R.styleable.MarcoPoloCell_topLeftText));
            setTopRightText(a.getString(R.styleable.MarcoPoloCell_topRightText));
            setBottomLeftText(a.getString(R.styleable.MarcoPoloCell_bottomLeftText));
            setBottomRightText(a.getString(R.styleable.MarcoPoloCell_bottomRightText));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        } finally {
            a.recycle();
        }

        debug();
    }

    private void debug() {
        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.marcopolo_swipe_layout);
        RelativeLayout topWrapper = (RelativeLayout) findViewById(R.id.top_wrapper);
        OnClickListener ocl = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked " + v.toString() + "!");
            }
        };
        swipeLayout.setOnClickListener(ocl);
        topWrapper.setOnClickListener(ocl);
        center.setOnClickListener(ocl);
        this.setOnClickListener(ocl);
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        RelativeLayout topWrapper = (RelativeLayout) findViewById(R.id.top_wrapper);
        topWrapper.setBackgroundColor(color);
    }

    public void setDark() {}

    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
        switch (cellType) {
            case MARCO:
                setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case MARCO_DARK:
                setBackgroundColor(getResources().getColor(R.color.dark_blue));
                break;
            case POLO:
                setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case POLO_DARK:
                setBackgroundColor(getResources().getColor(R.color.dark_blue));
                break;
            case FRIEND:
                setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case FRIEND_DARK:
                setBackgroundColor(getResources().getColor(R.color.dark_green));
                break;
            case CONTACT:
                setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case CONTACT_DARK:
                setBackgroundColor(getResources().getColor(R.color.dark_green));
                break;
        }
        invalidate();
        requestLayout();
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        center.setTextColor(textColor);
        topLeft.setTextColor(textColor);
        topRight.setTextColor(textColor);
        bottomLeft.setTextColor(textColor);
        bottomRight.setTextColor(textColor);
    }

    public String getCenterText() {
        return center.getText().toString();
    }

    public void setCenterText(String centerText) {
        this.center.setText(centerText);
        invalidate();
        requestLayout();
    }

    public String getTopLeftText() {
        return topLeft.getText().toString();
    }

    public void setTopLeftText(String topLeftText) {
        this.topLeft.setText(topLeftText);
        invalidate();
        requestLayout();
    }

    public String getTopRightText() {
        return topRight.getText().toString();
    }

    public void setTopRightText(String topRightText) {
        this.topRight.setText(topRightText);
        invalidate();
        requestLayout();
    }

    public String getBottomLeftText() {
        return bottomLeft.getText().toString();
    }

    public void setBottomLeftText(String bottomLeftText) {
        this.bottomLeft.setText(bottomLeftText);
        invalidate();
        requestLayout();
    }

    public String getBottomRightText() {
        return bottomRight.getText().toString();
    }

    public void setBottomRightText(String bottomRightText) {
        this.bottomRight.setText(bottomRightText);
        invalidate();
        requestLayout();
    }
}
