package com.caimuhao.rxpicker.bean;

/**
 * @author Smile
 * @time 2017/4/20  上午9:26
 * @desc ${TODD}
 */
public class FolderClickEvent {

  private int position;
  private MediaFolder folder;

  public FolderClickEvent(int position, MediaFolder folder) {
    this.position = position;
    this.folder = folder;
  }

  public MediaFolder getFolder() {
    return folder;
  }

  public int getPosition() {
    return position;
  }
}
