package com.farmerfirst.growagric.ui.learning.db.course;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Course.class}, version = 2)
public abstract class CourseDatabase extends RoomDatabase {

    private static CourseDatabase INSTANCE;
    public abstract ICourseDao courseDao();
    private final static Object sLock = new Object();

    private final static List<Course> COURSES = new ArrayList<>();

    public static CourseDatabase getInstance(Context context){
        synchronized(sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),CourseDatabase.class,"courses")
                        .allowMainThreadQueries()
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).courseDao().saveAll(COURSES));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
