package com.example.thunderbird;

import android.app.Application;
import android.widget.Toast;

public class myToast {
    public static void toastStatic(Application app, String s){
        Toast.makeText(app, s, Toast.LENGTH_SHORT).show();
    }
}
