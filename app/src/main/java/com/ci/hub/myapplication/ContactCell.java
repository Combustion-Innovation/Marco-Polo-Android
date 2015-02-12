package com.ci.hub.myapplication;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Alex on 1/31/15.
 */
public class ContactCell extends MarcoPoloCell {
    public final static String TAG = "MarcoPoloCell";

    public ContactCell(Context context) {
        super(context);
        setCellType(CONTACT);
    }

    public ContactCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCellType(CONTACT);
    }

    public void setDark() {
        setCellType(CONTACT_DARK);
    }
}
