package com.farmerfirst.growagric.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerfirst.growagric.ui.message.chat.db.ChatDatabase;
import com.farmerfirst.growagric.ui.message.chat.db.INotificationDao;
import com.farmerfirst.growagric.ui.message.chat.db.Notification;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsAndroidViewModel extends AndroidViewModel {
    private INotificationDao notificationDao;
    private ExecutorService executorService;

    public NotificationsAndroidViewModel(@NonNull Application application){
        super(application);
        notificationDao = ChatDatabase.getInstance(application).notificationDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Notification>> getAllNotifications(){
        return notificationDao.getAll();
    }

    public List<Notification> getAllNotificationsSync(){
        return notificationDao.getAllSync();
    }

    public int getNotificationCNT(){
        return notificationDao.getCount();
    }

    public LiveData<List<Notification>> getSearchedNotificationMessage(String search_input){
        return notificationDao.searchNotification(search_input);
    }

    public void saveNotification(Notification notification){
        executorService.execute(()->notificationDao.save(notification));
    }

    public void deleteNotification(Notification notification){
        executorService.execute(()->notificationDao.delete(notification));
    }

    public void deleteAll(){
        notificationDao.deleteAll();
    }
}
