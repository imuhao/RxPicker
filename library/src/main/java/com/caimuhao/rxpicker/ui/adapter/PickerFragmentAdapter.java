package com.caimuhao.rxpicker.ui.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.caimuhao.rxpicker.PickerConfig;
import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.RxPickerManager;
import com.caimuhao.rxpicker.bean.MediaItem;
import com.caimuhao.rxpicker.utils.RxBus;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/19  上午10:43
 * @desc ${TODD}
 */
public class PickerFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int CAMERA_TYPE = 0;
  private static final int NORMAL_TYPE = 1;

  private View.OnClickListener cameraClickListener;

  private int imageWidth;
  private PickerConfig config;

  private List<MediaItem> datas;
  private List<MediaItem> checkImage;

  public PickerFragmentAdapter(int imageWidth) {
    config = RxPickerManager.getInstance().getConfig();
    this.imageWidth = imageWidth;
    checkImage = new ArrayList<>();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (CAMERA_TYPE == viewType) {
      return new CameraViewHolder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false));
    } else {
      return new PickerViewHolder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CameraViewHolder) {
      holder.itemView.setOnClickListener(cameraClickListener);
      return;
    }
    int dataPosition = config.isShowCamera() ? position - 1 : position;

    final MediaItem mediaItem = datas.get(dataPosition);
    PickerViewHolder pickerViewHolder = (PickerViewHolder) holder;
    pickerViewHolder.bind(mediaItem);

    pickerViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (config.isSingle()) {
          RxBus.singleton().post(mediaItem);
        } else {
          boolean b = checkImage.contains(mediaItem) ? checkImage.remove(mediaItem)
              : checkImage.add(mediaItem);
          notifyItemChanged(holder.getAdapterPosition());
        }
      }
    });
  }

  @Override public int getItemCount() {
    if (datas != null && config.isShowCamera()) {
      return datas.size() + 1;
    } else if (datas != null) {
      return datas.size();
    }
    return 0;
  }

  @Override public int getItemViewType(int position) {
    if (config.isShowCamera() && position == 0) {
      return CAMERA_TYPE;
    } else {
      return NORMAL_TYPE;
    }
  }

  public void setDatas(List<MediaItem> datas) {
    this.datas = datas;
  }

  public void setCameraClickListener(View.OnClickListener cameraClickListener) {
    this.cameraClickListener = cameraClickListener;
  }

  public ArrayList<MediaItem> getCheckImage() {
    return (ArrayList<MediaItem>) checkImage;
  }

  private class PickerViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private AppCompatCheckBox cbCheck;

    private PickerViewHolder(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.iv_image);
      cbCheck = (AppCompatCheckBox) itemView.findViewById(R.id.cb_check);
    }

    private void bind(MediaItem mediaItem) {
      RxPickerManager.getInstance().display(imageView, mediaItem.getPath(), imageWidth, imageWidth);
      cbCheck.setVisibility(config.isSingle() ? View.GONE : View.VISIBLE);
      cbCheck.setChecked(checkImage.contains(mediaItem));
    }
  }

  private class CameraViewHolder extends RecyclerView.ViewHolder {

    private CameraViewHolder(View itemView) {
      super(itemView);
    }
  }
}


