package com.caimuhao.sample;

import android.app.Application;
import com.caimuhao.rxpicker.RxPicker;

/**
 * @author Smile
 * @time 2017/4/19  下午12:44
 * @desc ${TODD}
 */
public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    RxPicker.init(new GlideImageLoader());
  }
}
