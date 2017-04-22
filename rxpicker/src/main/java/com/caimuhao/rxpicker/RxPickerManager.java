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

  public RxPickerManager setConfig(PickerConfig config) {
    this.config = config;
    return this;
  }

  public PickerConfig getConfig() {
    return config;
  }

  public void init(RxPickerImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setMode(PickerConfig.Mode mode) {
    config.setMode(mode);
  }

  public void showCamera(boolean showCamera) {
    config.setShowCamera(showCamera);
  }

  public void display(ImageView imageView, String path, int width, int height) {
    if (imageLoader == null) {
      throw new NullPointerException(
          "you must call  RxPickerManager.getInstance().setImageLoad()  set custom ImageLoad");
    }
    imageLoader.display(imageView, path, width, height);
  }

  public List<ImageItem> getResult(Intent intent) {
    return (List<ImageItem>) intent.getSerializableExtra(PickerFragment.MEDIA_RESULT);
  }

  public void limit(int limit) {
    config.setMaxValue(limit);
  }
}
