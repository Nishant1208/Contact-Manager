package com.example.contactmanager;

import static android.Manifest.permission.READ_CONTACTS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.adapter.ContactAdapter;
import com.example.contactmanager.model.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactList;
    private ContactAdapter adapter;
    private AppCompatButton addContactBtn;
    private final int PICK_CONTACT = 1;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get the group name from the intent
        groupName = getIntent().getStringExtra("groupName");

        // Display the group name
        TextView groupNameTextView = findViewById(R.id.group_name);
        groupNameTextView.setText(groupName);

        // Initialize the RecyclerView and the adapter
        recyclerView = findViewById(R.id.contact_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        adapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(adapter);

        // Add the contacts from the SharedPreferences
        loadContacts();

        // Initialize the "Add Contact" button
        addContactBtn = findViewById(R.id.add_contact_button);
        addContactBtn.setOnClickListener(view -> {
            // Check for permission to read contacts
            if (ContextCompat.checkSelfPermission(DetailsActivity.this, String.valueOf(new String[]{READ_CONTACTS})) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{READ_CONTACTS}, PICK_CONTACT);
            } else {
                // Permission already granted, launch the contact picker
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        });

        // Initialize the "Back" button
        ImageView backButton = findViewById(R.id.back_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the contacts to the SharedPreferences
                saveContacts();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                // Add the selected contact to the list
                Contact contact = Contact.getContactFromUri(this, data.getData());
                if (contact != null) {
                    contactList.add(contact);
                    adapter.notifyDataSetChanged();
                    saveContacts();
                } else {
                    Toast.makeText(this, "Invalid contact selected", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No contact selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Saves the current list of contacts to SharedPreferences.
     */
    private void saveContacts() {
        // Convert the contact list to a JSON string
        Gson gson = new Gson();
        String json = gson.toJson(contactList);

        // Save the JSON string to the SharedPreferences
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(groupName, json);
        editor.apply();
    }


    /**
     * Loads the list of contacts from SharedPreferences and displays them in the RecyclerView.
     */
    private void loadContacts() {
        // Load the JSON string from the SharedPreferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(groupName, "");
        if (!json.isEmpty()) {
            // Convert the JSON string to a list of contacts
            Gson gson = new Gson();
            Type type = new TypeToken<List<Contact>>() {}.getType();
            List<Contact> contacts = gson.fromJson(json, type);
            contactList.addAll(contacts);
            adapter.notifyDataSetChanged();
        }
    }
}
