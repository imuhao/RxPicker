package com.caimuhao.rxpicker.utils;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Smile
 * @time 2017/4/18  下午6:07
 * @desc ${TODD}
 */
public class PickerConfig {

  private int minValue = 1;
  private int maxValue = 9;
  private boolean showCamera = true;

  public static final int SINGLE_IMG = 0x001;
  public static final int MULTIPLE_IMG = 0x002;
  private int mode = SINGLE_IMG;

  @IntDef({ SINGLE_IMG, MULTIPLE_IMG }) @Retention(RetentionPolicy.SOURCE) @interface Mode {
  }

  public int getMode() {
    return mode;
  }

  public void setMode(@Mode int mode) {
    this.mode = mode;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public int getMinValue() {
    return minValue;
  }

  public void setLimit(int minValue, int maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public boolean isShowCamera() {
    return showCamera;
  }

  public void setShowCamera(boolean showCamera) {
    this.showCamera = showCamera;
  }

  public boolean isSingle() {
    return mode == SINGLE_IMG;
  }
}
