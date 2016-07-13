package com.kiwi.auready_ver2.data.local;

/**
 * The contract used for the db to save tables locally.
 */
public final class PersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FriendEntry {
        public static final String TABLE_NAME = "friend";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_EMAIL = "email";
    }
}
