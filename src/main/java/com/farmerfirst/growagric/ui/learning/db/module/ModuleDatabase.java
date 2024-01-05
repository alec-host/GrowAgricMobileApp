package com.farmerfirst.growagric.ui.learning.db.module;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.IFarmDao;
import com.farmerfirst.growagric.ui.learning.db.course.Course;
import com.farmerfirst.growagric.ui.learning.db.course.CourseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Module.class}, version = 1)
public abstract class ModuleDatabase extends RoomDatabase{

    private static ModuleDatabase INSTANCE;
    public abstract IModuleDao moduleDao();
    private final static Object sLock = new Object();

    private final static List<Module> MODULES = new ArrayList<>();

    public static ModuleDatabase getInstance(Context context){
        synchronized(sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ModuleDatabase.class,"modules")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback(){
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db){
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).moduleDao().saveAll(MODULES));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}