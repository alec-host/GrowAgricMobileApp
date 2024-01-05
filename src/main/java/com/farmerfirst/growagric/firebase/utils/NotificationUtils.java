package com.farmerfirst.growagric.firebase.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.firebase.vo.NotificationVO;
import com.farmerfirst.growagric.ui.Splash;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NotificationUtils{

    private static final int NOTIFICATION_ID = 200;
    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    private  Map<String, Class> activityMap = new HashMap<>();
    private final Context mContext;
    private int numMessages;
    private PendingIntent resultPendingIntent;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //-.Populate activity map.
        activityMap.put("PrimaryActivity", Splash.class);
        activityMap.put("SecondActivity", MainActivity.class);
        this.numMessages = 1;
    }
    /**
     * Displays notification based on parameters
     *
     */
    public void displayNotification(@NonNull NotificationVO notificationVO, Intent resultIntent) {
        {
            String message = notificationVO.getMessage();
            String title = notificationVO.getTitle();
            String iconUrl = notificationVO.getImageURL();
            String action = notificationVO.getAction();
            String destination = notificationVO.getActionDestination();
            Bitmap iconBitMap = null;
            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl);
            }
            final int icon = R.mipmap.ic_launcher;

            LocalSharedPreferences prefs = new LocalSharedPreferences(mContext);

            //-.increment the counter.
            prefs.setChatHeadCounter(String.valueOf(Integer.parseInt(prefs.getChatHeadCounter()) + numMessages));

            if (URL.equals(action)) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
                resultIntent = new Intent(mContext, activityMap.get(destination));
                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            }else{
                    try {
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        resultPendingIntent =
                                PendingIntent.getActivity(
                                        mContext,
                                        0,
                                        resultIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

            Notification notification;

            String modified_message = "";

            if(Integer.parseInt(prefs.getChatHeadCounter()) == 0) {
                modified_message =  message;
            }else{
                modified_message = Integer.parseInt(prefs.getChatHeadCounter()) +""+ System.getProperty("line.separator") +""+ message;
            }

            if(iconBitMap == null){
                //-.When Inbox Style is applied, user can expand the notification.
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);

                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(System.currentTimeMillis())
                        .setNumber(Integer.parseInt(prefs.getChatHeadCounter()))
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(modified_message)
                        .build();
            } else {
                //-.If Bitmap is created from URL, show big icon.
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(iconBitMap);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(System.currentTimeMillis())
                        .setNumber(Integer.parseInt(prefs.getChatHeadCounter()))
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(modified_message)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            //-.All notifications should go through NotificationChannel on Android 26 & above.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);

            }
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
    @Nullable
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void playNotificationVibration(){
        String channel_id;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel_id = "1";
        }else{
            channel_id = "";
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, channel_id);
        notificationBuilder.setLights(Color.RED, 3000, 3000);
        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationBuilder.build();
    }
}
