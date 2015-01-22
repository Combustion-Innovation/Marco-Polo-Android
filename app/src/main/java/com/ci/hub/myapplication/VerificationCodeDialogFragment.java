package com.ci.hub.myapplication;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

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

    private String verificationCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_verification_dialog, container);
        getDialog().setTitle("PHONE VERIFICATION");

        view.findViewById(R.id.verification_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        view.findViewById(R.id.verification_resend_code_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // not the cleanest but it works
                ((SignUpActivity) getActivity()).sendVerificationCode();
            }
        });

        view.findViewById(R.id.verification_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ((EditText) view.findViewById(R.id.verification_code_field)).getText().toString();
                if (code.length() < 5) {    // if code is too short
                    Toast.makeText(getActivity(), "The verification code is 5 digits long", Toast.LENGTH_LONG).show();
                } else if (code.equals(verificationCode)) {  // code is correct
                    ((SignUpActivity) getActivity()).signUp();
                } else {    // code is false
                    Log.d(TAG, "verificationCode: " + verificationCode);
                    Log.d(TAG, "code: " + code);
                    Toast.makeText(getActivity(), "Incorrect verification code", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setVerificationCode(String code) {
        verificationCode = code;
    }
}
