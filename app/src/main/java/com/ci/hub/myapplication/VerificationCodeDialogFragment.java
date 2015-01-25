package com.ci.hub.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
                if (code.length() == 0) {    // if code is too short
                    Toast.makeText(getActivity(), "You must enter the verification code", Toast.LENGTH_LONG).show();
                } else {
                    ((VerificationCodeCallback) getActivity()).onVerification(code);
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
}
