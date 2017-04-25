package com.caimuhao.rxpicker.utils;

import android.content.Context;

/**
 * @author Smile
 * @time 2017/4/25  下午9:41
 * @desc 用于解决provider冲突的util
 */
public class ProviderUtil {

  public static String getFileProviderName(Context context) {
    return context.getPackageName() + ".provider";
  }
}
