package com.ci.contactmanager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    private Activity activity;
    private boolean contactPermission;
    private CICallback onPermissionsCallback;

    public ContactManager(Activity activity) {
        this.activity = activity;
    }

    private List getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>(2);

        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                Contact contact = null;
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //Log.d(TAG, "name: " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.d(TAG, "phone number: " + phone);
                        contact = new Contact(name, phone);
                    }
                    pCur.close();
                    contacts.add(contact);
                } else {
                    Log.d(TAG, "getAllContacts(): the contact does not have a phone number.");
                }
            }
        }

        return contacts;
    }

    public boolean hasContactPermission() {
        return contactPermission;
    }

    public void getContacts(CICallback callback) {
        onPermissionsCallback = callback;

        if (hasContactPermission()) {
            onPermissionsCallback.onEnd(getAllContacts());
        } else {
            onPermissionsCallback.onStart(null);
            askForContactsPermission();
        }
    }

    public void askForContactsPermission() {
        GetContactsConfirmationFragment confirmationDialog = new GetContactsConfirmationFragment();
        confirmationDialog.setCallback(
                new GetContactsConfirmationFragment.ConfirmationCallback() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog, int id) {
                        contactPermission = true;
                        onPermissionsCallback.onEnd(getAllContacts());
                    }

                    @Override
                    public void onNegativeButton(DialogInterface dialog, int id) {
                        contactPermission = false;
                    }
                }
        );
        confirmationDialog.show(activity.getFragmentManager(), "ContactsDialog");
    }
}
