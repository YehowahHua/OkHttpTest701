package com.yehowah.sample;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 2017/8/30.
 */

public class L {
    private static boolean debug = true;

    public  static  void e(String msg){
        if(debug){
            Log.e(TAG, msg );
        }
    }
}
