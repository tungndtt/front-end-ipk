package com.example.tintok.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String ConvertTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return  time.format(formatter);
    }
}
