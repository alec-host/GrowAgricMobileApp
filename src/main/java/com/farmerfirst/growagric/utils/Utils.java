package com.farmerfirst.growagric.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.farmerfirst.growagric.App;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.MediaType;

public class Utils {
    public static String removeLeadZero(@NonNull String hayStack){
        final String needle = "0";
        final int needleSize = needle.length();

        return hayStack.startsWith(needle) ? hayStack.substring(needleSize) : hayStack;
    }
    public static String getCommalessNumber(String commaNumber){
        String newNumber = "";
        String[] split = commaNumber.trim().split(",");
        for(int i = 0; i < split.length; i++){
            newNumber += split[i];
        }
        return newNumber;
    }
    public static String formatWithCommas(String input) {
        StringBuilder stringBuilder = new StringBuilder(input);
        int index = stringBuilder.length() - 3;
        while (index > 0) {
            stringBuilder.insert(index, ",");
            index -= 3;
        }
        return stringBuilder.toString();
    }
    public static long generateRandomNumber(long min, long max) {
        Random random = new Random();
        return random.nextLong() % (max - min + 1) + min;
    }
    public static void createLearningMaterialFolder(Context context){
        File appFolder = new File(context.getApplicationContext().getFilesDir(),"GrowAgricLearn");
        if(!appFolder.exists()){
            appFolder.mkdir();
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX CREATE DIR  NEW : GrowAgricLearn");
        }else{
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX EXISTS : GrowAgricLearn");
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX CREATE DIR : GrowAgricLearn");
    }
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static SimpleDateFormat simpleDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        return sdf;
    }

    public static String getRealPathFromURI(Uri uri) {
        String result;
        Cursor cursor = App.getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}