package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.contactmanager.adapter.GroupAdapter;
import com.example.contactmanager.db.GroupDatabaseHelper;
import com.example.contactmanager.model.Group;
import com.example.contactmanager.data.GroupContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Group> groupList;
    private GroupAdapter groupAdapter;
    private CardView cardView;
    private ImageView addGroupIB;
    private AppCompatButton addGroupBtn;

    private GroupDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.group_recycler_view);
        cardView = findViewById(R.id.cardview);
        addGroupIB = findViewById(R.id.addGroupIB);
        addGroupBtn = findViewById(R.id.addGroupBtn);

        groupList = new ArrayList<>();
        dbHelper = new GroupDatabaseHelper(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        groupAdapter = new GroupAdapter(this, groupList, this);
        recyclerView.setAdapter(groupAdapter);

        addGroupIB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
            startActivity(intent);
        });

        addGroupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroupsFromDatabase();
    }

    private void loadGroupsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                GroupContract.GroupEntry._ID,
                GroupContract.GroupEntry.COLUMN_NAME,
                GroupContract.GroupEntry.COLUMN_IMAGE
        };

        Cursor cursor = db.query(
                GroupContract.GroupEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Group> newGroupList = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(GroupContract.GroupEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.GroupEntry.COLUMN_NAME));
            int image = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.GroupEntry.COLUMN_IMAGE)));
            newGroupList.add(new Group(id, image, name));
        }

        cursor.close();

        groupList.clear();
        groupList.addAll(newGroupList);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Group group) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("groupName", group.getName());
        intent.putExtra("groupImage", group.getImage());
        startActivity(intent);
    }
}
