package com.farmerfirst.growagric.ui.terms;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.farmerfirst.growagric.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CustomDialog{

    public static void show(Context context){
        try {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_list_dialog);
            WebView wv = dialog.findViewById(R.id.webViewBottom);
            AppCompatButton button = dialog.findViewById(R.id.buttonClose);
            button.setVisibility(View.VISIBLE);
            AssetManager manager = context.getAssets();
            InputStream stream = manager.open("terms.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            wv.getSettings().setSupportZoom(false);
            wv.setHorizontalScrollBarEnabled(true);

            wv.loadDataWithBaseURL(null, builder.toString(), "text/html", "UTF-8", null);

            dialog.setCancelable(false);
            dialog.show();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
