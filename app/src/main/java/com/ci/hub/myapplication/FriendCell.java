package com.ci.hub.myapplication;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Alex on 1/31/15.
 */
public class FriendCell extends MarcoPoloCell {
    public final static String TAG = "FriendCell";

    public FriendCell(Context context) {
        super(context);
        setCellType(FRIEND);
    }

    public FriendCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCellType(FRIEND);
    }

    public void setDark() {
        setCellType(FRIEND_DARK);
    }
}
