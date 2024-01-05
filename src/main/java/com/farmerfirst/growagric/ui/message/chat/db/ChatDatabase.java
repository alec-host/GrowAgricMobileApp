package com.farmerfirst.growagric.ui.message.chat.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Chat.class,Notification.class}, version = 2)
public abstract class ChatDatabase extends RoomDatabase {

    private static ChatDatabase INSTANCE;
    public abstract IChatDao chatDao();
    public abstract INotificationDao notificationDao();
    private final static Object sLock = new Object();

    private final static List<Chat> CHATS = new ArrayList<>();
    private final static List<Notification> NOTIFICATIONS = new ArrayList<>();

    public static ChatDatabase getInstance(Context context){
        synchronized(sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ChatDatabase.class,"chats")
                    .allowMainThreadQueries()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> getInstance(context).chatDao().saveAll(CHATS));
                            Executors.newSingleThreadExecutor().execute(() -> getInstance(context).notificationDao().saveAll(NOTIFICATIONS));
                        }
                    }).fallbackToDestructiveMigration().build();
            }
            return INSTANCE;
        }
    }
}
