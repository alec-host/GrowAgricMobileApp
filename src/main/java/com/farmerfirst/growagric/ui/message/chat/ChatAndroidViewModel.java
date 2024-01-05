package com.farmerfirst.growagric.ui.message.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerfirst.growagric.ui.message.chat.db.Chat;
import com.farmerfirst.growagric.ui.message.chat.db.ChatDatabase;
import com.farmerfirst.growagric.ui.message.chat.db.IChatDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatAndroidViewModel extends AndroidViewModel {
    private IChatDao chatDao;
    private ExecutorService executorService;

    public ChatAndroidViewModel(@NonNull Application application){
        super(application);
        chatDao = ChatDatabase.getInstance(application).chatDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Chat>> getAllChats(String uuid){
        return chatDao.getAll(uuid);
    }

    public List<Chat> getAllChatsSynchronously(String uuid){
        return chatDao.getAllSync(uuid);
    }

    public int getChatCNT(String uuid){
        return chatDao.getCount(uuid);
    }

    public LiveData<List<Chat>> getSearchedChatMessage(String searchedChat){
        return chatDao.searchChatMessage(searchedChat);
    }

    public void saveChat(Chat chat){
        executorService.execute(()->chatDao.save(chat));
    }

    public void deleteChat(Chat chat){
        executorService.execute(()->chatDao.delete(chat));
    }

    public void deleteAll(){
        chatDao.deleteAll();
    }
}