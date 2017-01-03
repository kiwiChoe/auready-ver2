package com.kiwi.auready_ver2.data.source.local;

/**
 * The contract used for the db to save tables locally.
 */
final class PersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PersistenceContract() {}

    /* TAG */
    static class DBExceptionTag {
        static final String TAG_SQLITE = "SQLiteException: ";
    }
    /* Inner class that defines the table contents */
    static class FriendEntry {
        static final String TABLE_NAME = "friend";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_EMAIL = "email";
        static final String COLUMN_NAME = "name";
    }

    static class MemberEntry {
        static final String TABLE_NAME = "member";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_HEAD_ID_FK = "taskheadid";
        static final String COLUMN_FRIEND_ID_FK = "friendid";
        static final String COLUMN_NAME = "name";
    }

    static class TaskHeadEntry {
        static final String TABLE_NAME = "taskhead";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_ORDER = "sequence";
    }

    static class TaskEntry {
        static final String TABLE_NAME = "task";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_MEMBER_ID_FK = "memberid";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_COMPLETED = "completed";
        static final String COLUMN_ORDER = "sequence";
    }
}
