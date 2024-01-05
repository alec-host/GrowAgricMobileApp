package com.farmerfirst.growagric.ui.finance.recyclerview.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Finance.class},version = 1)
public abstract class FinanceDatabase extends RoomDatabase {

    private static FinanceDatabase INSTANCE;
    public abstract IFinanceDao financeDao();
    private final static Object sLock = new Object();

    private final static List<Finance> FINANCES = new ArrayList<>();

    public static FinanceDatabase getInstance(Context context){
        synchronized (sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),FinanceDatabase.class,"finance")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).financeDao().saveAll(FINANCES));
                            }
                        })
                        .fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
