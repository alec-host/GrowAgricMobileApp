package com.farmerfirst.growagric.ui.farm.recyclerview.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Farm.class}, version = 1)
public abstract class FarmDatabase extends RoomDatabase {

    private static FarmDatabase INSTANCE;
    public abstract IFarmDao farmDao();
    private final static Object sLock = new Object();

    private final static List<Farm> FARMS = new ArrayList<>();

    public static FarmDatabase getInstance(Context context){
        synchronized(sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),FarmDatabase.class,"farms")
                    .allowMainThreadQueries()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> getInstance(context).farmDao().saveAll(FARMS));
                        }
                    }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
