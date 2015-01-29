package com.ci.hub.contactmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 1/7/15.
 */

/*
 * Julian's Asana instructions:
 * make an api to get contacts from a users phone book. Methods should include:

 Did user agree to getcontacts

 bool

 getContactsNumbers

 getContactsNames

 startGettingContacts
 */

public class ContactManager {
    public static final String TAG = "ContactManager";

    private static Activity activity;
    private static boolean contactPermission;

    private static List getAllContacts() {
        List<HashMap<String, String>> contacts = new ArrayList<HashMap<String, String>>(2);

        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                HashMap<String, String> contact = new HashMap<String, String>();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.put("name", name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone" + phone);
                        contact.put("phone", phone);
                    }
                    pCur.close();
                }
                contacts.add(contact);
            }
        }

        return contacts;
    }

    public ContactManager(Activity activity) {
        this.activity = activity;
        contactPermission = false;

        getAllContacts();
    }

    public boolean hasContactPermission() {
        return contactPermission;
    }

    public List<String> getContactsNumbers() {
        //if (hasContactPermission()) {
            // collect numbers...
            List<HashMap<String, String>> contacts = getAllContacts();
            List numbers = new ArrayList<String>();
            for (int i = 0; i < contacts.size(); i++) {
                numbers.add(contacts.get(i).get("phone"));
            }
            return numbers;
        //}
        //return null;
    }

    public List<String> getContactsNames() {
        //if (hasContactPermission()) {
            // collect names
            List<HashMap<String, String>> contacts = getAllContacts();
            List names = new ArrayList<String>();
            for (int i = 0; i < contacts.size(); i++) {
                names.add(contacts.get(i).get("name"));
            }
            return names;
        //}
        //return null;
    }

    public void askForContactsPermission() {
        GetContactsConfirmationFragment confirmationDialog = new GetContactsConfirmationFragment();
        confirmationDialog.show(activity.getFragmentManager(), "ContactsDialog");
    }

    public static class GetContactsConfirmationFragment extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Allow this app to access your contacts?")
                    .setTitle("Contacts")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "We have permission!");
                            contactPermission = true;
                            getAllContacts();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "We don't have permission!");
                            contactPermission = false;
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
