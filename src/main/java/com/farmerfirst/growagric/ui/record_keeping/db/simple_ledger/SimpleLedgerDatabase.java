package com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {SimpleLedger.class}, version = 1)
public abstract class SimpleLedgerDatabase extends RoomDatabase {
    private static SimpleLedgerDatabase INSTANCE;
    public abstract ISimpleLedgerDao simpleLedgerDao();
    private final static Object slock = new Object();

    private final static List<SimpleLedger> SIMPLE_LEDGERS = new ArrayList<>();

    public static SimpleLedgerDatabase getInstance(Context context) {
        synchronized (slock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SimpleLedgerDatabase.class, "simple_ledger")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).simpleLedgerDao().saveAll(SIMPLE_LEDGERS));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
