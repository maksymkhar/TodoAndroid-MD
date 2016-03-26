package com.iesebre.dam2.max.todosandroidmd.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iesebre.dam2.max.todosandroidmd.R;

/**
 * Created by max on 10/12/15.
 */
public class Utils {

    public static void displayToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUsingWifi (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null)
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { return true; }
        }

        return false;
    }

    public static void displaySimpleDialog(Context context, String title, String message)
    {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.ok).toUpperCase())
                .show();
    }

    public static void vibrate(Context context, int duration)
    {
        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(duration);
    }

}
