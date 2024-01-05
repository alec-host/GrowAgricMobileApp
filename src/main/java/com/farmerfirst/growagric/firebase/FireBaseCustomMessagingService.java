package com.farmerfirst.growagric.firebase;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Looper;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.firebase.utils.NotificationUtils;
import com.farmerfirst.growagric.firebase.vo.NotificationVO;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.message.chat.ChatActivity;
import com.farmerfirst.growagric.ui.message.chat.db.Chat;
import com.farmerfirst.growagric.ui.message.chat.db.ChatDatabase;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

public class FireBaseCustomMessagingService extends FirebaseMessagingService {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String ACTION_DESTINATION = "action_destination";

    private Intent resultIntent;
    private String[] payloadData;

    @Override
    public void onNewToken(@NonNull String my_token){
        super.onNewToken(my_token);
        //-.write to console.
        System.out.println("NEW_TOKEN " + my_token.trim());
        //-.background task.
        final LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(()->{
            if(prefs.getFcmToken().equals("")){
                prefs.setFcmToken(my_token.trim());
            }else{
                prefs.deleteFcmToken();
                prefs.setFcmToken(my_token.trim());
            }
        });
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message){
        if(message.getData().size() > 0){
            Map<String, String> data = message.getData();
            handleData(data);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch(Objects.requireNonNull(message.getData().get("key4")).trim()){
                        case "chat":
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                            LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
                            Long chat_uuid = Utils.generateRandomNumber(100000L,999999L);
                            String user_uuid = LocalData.GetUserUUID(prefs);
                            List<Chat> CHATS;
                            String chat_message = Objects.requireNonNull(message.getData().get("key3")).trim();
                            int message_origin = 1;
                            CHATS = Collections.singletonList(new Chat(String.valueOf(chat_uuid),user_uuid,chat_message,message_origin,sdf.format(new Date())));
                            Executors.newSingleThreadExecutor().execute(() -> ChatDatabase.getInstance(App.getContext()).chatDao().saveAll(CHATS));
                        break;
                    }
                }
            },2500);
        }else if(message.getNotification() != null){
            System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ 444444444444444444444444444444");
            handleNotification(message.getNotification());
        }
    }
    private void handleNotification(@NonNull RemoteMessage.Notification notification){
        String message = notification.getBody();
        String title = notification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        final LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());

        if (prefs.isLogin()) {
            resultIntent = new Intent(App.getContext(), MainActivity.class);
        }else{
            resultIntent = new Intent(App.getContext(), LoginActivity.class);
        }

        NotificationUtils notificationUtils = new NotificationUtils(App.getContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationVibration();
    }
    private void handleData(@NonNull Map<String, String> data){
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setImageURL(iconUrl);
        notificationVO.setAction(action);
        notificationVO.setActionDestination(actionDestination);

        /*
        final LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());

        if (prefs.isLogin()) {
            resultIntent = new Intent(App.getContext(), MainActivity.class);
        }else{
            resultIntent = new Intent(App.getContext(), LoginActivity.class);
        }

         */
        resultIntent = new Intent(App.getContext(), ChatActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(App.getContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationVibration();
    }
}
