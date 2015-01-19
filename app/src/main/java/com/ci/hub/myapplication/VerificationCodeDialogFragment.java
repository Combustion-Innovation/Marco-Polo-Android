package com.ci.hub.myapplication;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex on 1/18/15.
 */

/*
 use with:

 FragmentManager fm = getSupportFragmentManager();
 VerificationCodeDialogFragment editNameDialog = new VerificationCodeDialogFragment();
 editNameDialog.show(fm, TAG);

 */
public class VerificationCodeDialogFragment extends DialogFragment {
    public static final String TAG = "VerificationCodeDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification_dialog, container);
        getDialog().setTitle(TAG);

        return view;
    }
}
