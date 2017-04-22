package com.caimuhao.rxpicker;

import android.content.Intent;
import android.widget.ImageView;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.caimuhao.rxpicker.ui.fragment.PickerFragment;
import com.caimuhao.rxpicker.utils.RxPickerImageLoader;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/18  下午6:08
 * @desc ${TODD}
 */
public class RxPickerManager {

  private PickerConfig config;
  private static RxPickerManager manager;

  private RxPickerImageLoader imageLoader;

  public static RxPickerManager getInstance() {
    if (manager == null) {
      synchronized (RxPickerManager.class) {
        if (manager == null) {
          manager = new RxPickerManager();
        }
      }
    }
    return manager;
  }

  private RxPickerManager() {
  }

  RxPickerManager setConfig(PickerConfig config) {
    this.config = config;
    return this;
  }

  public PickerConfig getConfig() {
    return config;
  }

  void init(RxPickerImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  void setMode(PickerConfig.Mode mode) {
    config.setMode(mode);
  }

  void showCamera(boolean showCamera) {
    config.setShowCamera(showCamera);
  }

  void limit(int limit) {
    config.setMaxValue(limit);
  }

  public void display(ImageView imageView, String path, int width, int height) {
    if (imageLoader == null) {
      throw new NullPointerException(
          "You must fist of all call 'RxPickerManager.getInstance().init()' to initialize");
    }
    imageLoader.display(imageView, path, width, height);
  }

  public List<ImageItem> getResult(Intent intent) {
    return (List<ImageItem>) intent.getSerializableExtra(PickerFragment.MEDIA_RESULT);
  }
}
