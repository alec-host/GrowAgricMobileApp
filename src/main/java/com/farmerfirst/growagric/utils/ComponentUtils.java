package com.farmerfirst.growagric.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.DecorToolbar;
import androidx.core.app.DialogCompat;
import androidx.viewpager.widget.ViewPager;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityAddEmployeeBinding;
import com.farmerfirst.growagric.databinding.ActivityFarmRecyclerviewBinding;
import com.farmerfirst.growagric.databinding.ActivityFinanceBinding;
import com.farmerfirst.growagric.ui.finance.FinanceActivity;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class ComponentUtils {
    //-.snack bar.
    public static void showSnackBar(View v, Object message, int dataType, int color){
        Snackbar snackbar;
        if(dataType == 1) {
            snackbar = Snackbar.make(v, (int) message, Snackbar.LENGTH_SHORT);
        }else {
            snackbar = Snackbar.make(v, (CharSequence) message, Snackbar.LENGTH_SHORT);
        }
        View snackBarView = snackbar.getView();
        TextView tv = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackBarView.setBackgroundResource(color);
        snackbar.show();
    }
    //-.action bar.
    public static void customActionBar(String title, AppCompatActivity activity){
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.custom_app_theme_color)));
        SpannableString s = new SpannableString(Html.fromHtml("<bold>"+title+"</bold>"));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        activity.getSupportActionBar().setTitle(s);
        activity.getSupportActionBar().setElevation(0);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_up32x32);

        Window window = activity.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.custom_app_theme_color));
        }
    }
    //-.dialog.
    public static AlertDialog customDialog(String message,Context context){
        AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setTitle("");
        dialog.setMessage(Html.fromHtml("<small>"+message+"...</small>"));
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public static void showDialog(Activity activity,String title,String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView textTitle = dialog.findViewById(R.id.textTitle);
        textTitle.setText(title.trim());
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        textMessage.setText(message.trim());
        AppCompatButton btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });
        dialog.show();
    }

    public static void showSuccessToast(View view,String message) {
        Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Context mContext) {
        try {
            View view = ((Activity) mContext).getWindow().getCurrentFocus();
            if (view != null && view.getWindowToken() != null) {
                IBinder binder = view.getWindowToken();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder, 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void showStatusBar(Activity av){
        Window window = av.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(params);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void customBottomDialog(View av,String prompt,String action){
         //int confirm = 0;
        //BottomDialog.Builder bottomDialog = new BottomDialog.Builder(av.getContext());
        //LayoutInflater inflater = (LayoutInflater) av.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.bottom_sheet_dialog,null);
        //bottomDialog.setCustomView(view);
//        AppCompatButton buttonCancel = view.findViewById(R.id.buttonCancel);
        //AppCompatButton buttonOk = view.findViewById(R.id.buttonOk);
        //buttonCancel.setOnClickListener(view1 -> bottomDialog.build().dismiss());

       // buttonOk.setOnClickListener(view1 -> Toast.makeText(view.getContext(),"nnnnnnnnnnnn",Toast.LENGTH_SHORT).show());

        //bottomDialog.setNegativeText(R.id.buttonCancel);


        //bottomDialog.setCancelable(true);
       // bottomDialog.show();
    }
}
