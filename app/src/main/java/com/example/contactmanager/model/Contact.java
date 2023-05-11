package com.example.contactmanager.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contact {
    private String name;
    private String phoneNumber;
    private String image;

    public Contact(String name, String phoneNumber, String image) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Contact getContactFromUri(Context context, Uri contactUri) {
        String name = null;
        String phoneNumber = null;
        String image = null;

        Cursor cursor = context.getContentResolver().query(contactUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            name = cursor.getString(nameIndex);

            @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

            if (phoneCursor != null && phoneCursor.moveToFirst()) {
                int phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNumber = phoneCursor.getString(phoneNumberIndex);
            }

            if (phoneCursor != null) {
                phoneCursor.close();
            }

            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            Cursor imageCursor = context.getContentResolver().query(photoUri,
                    new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);

            if (imageCursor != null && imageCursor.moveToFirst()) {
                byte[] imageData = imageCursor.getBlob(0);
                if (imageData != null) {
                    image = imageData.toString();
                }
            }

            if (imageCursor != null) {
                imageCursor.close();
            }

            cursor.close();
        }

        return new Contact(name, phoneNumber, image);
    }
}
