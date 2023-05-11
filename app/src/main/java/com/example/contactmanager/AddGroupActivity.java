package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactmanager.db.GroupDatabaseHelper;
import com.example.contactmanager.data.GroupContract;

public class AddGroupActivity extends AppCompatActivity {

    private EditText groupNameEditText;
    private AppCompatSpinner groupTypeSpinner;
    private AppCompatButton saveGroupButton;

    private GroupDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        groupNameEditText = findViewById(R.id.group_name_et);
        groupTypeSpinner = findViewById(R.id.group_type_spinner);
        saveGroupButton = findViewById(R.id.save_group_btn);

        dbHelper = new GroupDatabaseHelper(this);

        saveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroup();
            }
        });
    }

    private void saveGroup() {
        String groupName = groupNameEditText.getText().toString().trim();
        int groupImage = getGroupImage();

        if (groupName.isEmpty()) {
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GroupContract.GroupEntry.COLUMN_NAME, groupName);
        values.put(GroupContract.GroupEntry.COLUMN_IMAGE, groupImage);

        long newRowId = db.insert(GroupContract.GroupEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error saving group", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Group saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private int getGroupImage() {
        int position = groupTypeSpinner.getSelectedItemPosition();

        switch (position) {
            case 0:
                return R.drawable.ic_group;
            case 1:
                return R.drawable.ic_work;
            case 2:
                return R.drawable.ic_friends;
            case 3:
                return R.drawable.ic_family;
            default:
                return R.drawable.ic_group;
        }
    }
}

