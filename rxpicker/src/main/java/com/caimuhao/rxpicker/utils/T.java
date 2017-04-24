package com.caimuhao.rxpicker.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Smile
 * @time 2017/4/24  下午2:49
 * @desc ${TODD}
 */
public class T {
  
  public static void show(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
}
