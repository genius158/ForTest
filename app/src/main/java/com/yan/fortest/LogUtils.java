package com.yan.fortest;

import android.util.Log;
import android.view.View;

public class LogUtils {

  public static void e(View v) {
    Log.e("LogUtils", "e" + v);
  }
  public static void e() {
    Log.e("LogUtils", "e eeeeeeeeeeeeeeeeee");
  }
}
