package com.ci.hub.myapplication;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Alex on 1/31/15.
 */
public class MarcoCell extends MarcoPoloCell {
    public final static String TAG = "MarcoCell";

    public MarcoCell(Context context) {
        super(context);
        setCellType(MARCO);
    }

    public MarcoCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCellType(MARCO);
    }

    public void setDark() {
        setCellType(MARCO_DARK);
    }
}
