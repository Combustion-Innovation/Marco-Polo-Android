package com.ci.hub.contactmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Alex on 2/25/15.
 */
public class GetContactsConfirmationFragment extends DialogFragment {
    public final static String TAG = "GetContactsConfirmationFragment";

    private ConfirmationCallback callback;

    public GetContactsConfirmationFragment(ConfirmationCallback callback) {
        super();
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Allow this app to access your contacts?")
                .setTitle("Contacts")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "We have permission!");
                        callback.onPositiveButton(dialog, id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "We don't have permission!");
                        callback.onNegativeButton(dialog, id);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ConfirmationCallback {
        public void onPositiveButton(DialogInterface dialog, int id);
        public void onNegativeButton(DialogInterface dialog, int id);
    }
}