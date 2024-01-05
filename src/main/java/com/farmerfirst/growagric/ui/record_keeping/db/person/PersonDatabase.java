package com.farmerfirst.growagric.ui.record_keeping.db.person;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Person.class}, version = 1)
public abstract class PersonDatabase extends RoomDatabase {
    private static PersonDatabase INSTANCE;
    public abstract IPersonDao personDao();
    private final static Object slock = new Object();

    private final static List<Person> PERSONS = new ArrayList<>();

    public static PersonDatabase getInstance(Context context) {
        synchronized (slock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PersonDatabase.class, "person")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).personDao().saveAll(PERSONS));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
