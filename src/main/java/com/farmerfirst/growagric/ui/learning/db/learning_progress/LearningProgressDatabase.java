package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {LearningProgress.class,LearningProgressTracker.class}, version = 3)
public abstract class LearningProgressDatabase extends RoomDatabase {

    private static LearningProgressDatabase INSTANCE;
    public abstract ILearningProgressDao learningProgressDao();
    public abstract ILearningProgressTrackerDao learningProgressTrackerDao();

    private final static Object sLock = new Object();

    private final static List<LearningProgress> LEARNING_PROGRESSES = new ArrayList<>();
    private final static List<LearningProgressTracker> LEARNING_PROGRESS_TRACKERS = new ArrayList<>();

    public static LearningProgressDatabase getInstance(Context context){
        synchronized(sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),LearningProgressDatabase.class,"learnig_progress")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(() -> getInstance(context).learningProgressDao().saveAll(LEARNING_PROGRESSES));
                                Executors.newSingleThreadExecutor().execute(()->getInstance(context).learningProgressTrackerDao().saveAll(LEARNING_PROGRESS_TRACKERS));
                            }
                        }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
