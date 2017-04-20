package com.caimuhao.rxpicker;

/**
 * @author Smile
 * @time 2017/4/18  下午6:07
 * @desc ${TODD}
 */
public class PickerConfig {

  private Mode mode = Mode.SINGLE_IMG;
  private int maxValue = 9;
  private boolean crop = true;
  private boolean showCamera = true;

  public enum Mode {
    SINGLE_IMG, MULTIPLE_IMG
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }

  public boolean isCrop() {
    return crop;
  }

  public void setCrop(boolean crop) {
    this.crop = crop;
  }

  public boolean isShowCamera() {
    return showCamera;
  }

  public void setShowCamera(boolean showCamera) {
    this.showCamera = showCamera;
  }

  public boolean isSingle() {
    return mode == Mode.SINGLE_IMG;
  }
}
