package com.example.contactmanager.data;

import android.provider.BaseColumns;

// defines the database schema for the groups table
public final class GroupContract {

    private GroupContract() {}

    public static class GroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
    }
}
