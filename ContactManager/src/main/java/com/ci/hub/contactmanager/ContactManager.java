package com.ci.hub.contactmanager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
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

    private static Activity activity;
    private static boolean contactPermission;
    private static CICallback onPermissionsCallback;

    private static List getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>(2);

        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                Contact contact = null;
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name: " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone number: " + phone);
                        contact = new Contact(name, phone);
                    }
                    pCur.close();
                    contacts.add(contact);
                } else {
                    //log("getAllContacts(): the contact does not have a phone number.");
                    Log.d(TAG, "getAllContacts(): the contact does not have a phone number.");
                }
            }
        }

        return contacts;
    }

    public static void setActivity(Activity activity) {
        ContactManager.activity = activity;
    }

    public static boolean hasContactPermission() {
        return contactPermission;
    }

    private static void log(String msg) {
        Toast.makeText(activity, TAG + ": " + msg, Toast.LENGTH_LONG).show();
    }

    public static void getContacts(CICallback callback) {
        onPermissionsCallback = callback;

        if (hasContactPermission()) {
            onPermissionsCallback.onEnd(getAllContacts());
        } else {
            onPermissionsCallback.onStart(null);
            askForContactsPermission();
        }
    }

    public static void askForContactsPermission() {
        GetContactsConfirmationFragment confirmationDialog = new GetContactsConfirmationFragment(
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
