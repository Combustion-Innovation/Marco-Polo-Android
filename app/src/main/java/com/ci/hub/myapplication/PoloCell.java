package com.ci.hub.myapplication;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Alex on 1/31/15.
 */
public class PoloCell extends MarcoPoloCell {
    public final static String TAG = "PoloCell";

    public PoloCell(Context context) {
        super(context);
        setCellType(POLO);
    }

    public PoloCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCellType(POLO);
    }

    public void setDark() {
        setCellType(POLO_DARK);
    }
}
