package com.farmerfirst.growagric.ui.farm_visit.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public abstract class VisitDatabase extends RoomDatabase {
    private static VisitDatabase INSTANCE;
    public abstract IVisitDao visitDao();
    private final static Object slock = new Object();

    private final static List<Visit> VISITS = new ArrayList<>();

    public static VisitDatabase getInstance(Context context) {
        synchronized (slock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VisitDatabase.class, "farm_visit")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).visitDao().saveAll(VISITS));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
