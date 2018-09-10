package com.caimuhao.rxpicker.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.utils.RxPickerManager;
import com.caimuhao.rxpicker.bean.FolderClickEvent;
import com.caimuhao.rxpicker.bean.ImageFolder;
import com.caimuhao.rxpicker.utils.RxBus;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/19  下午5:06
 * @desc ${TODD}
 */
public class PickerAlbumAdapter extends RecyclerView.Adapter<PickerAlbumAdapter.ViewHolder> {

  private int imageWidth;
  private List<ImageFolder> datas;
  private int checkPosition = 0;

  private View.OnClickListener dismissListener;

  public PickerAlbumAdapter(List<ImageFolder> datas, int i) {
    this.datas = datas;
    imageWidth = i;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.bind(datas.get(position));

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismissListener.onClick(v);
        if (checkPosition == position) return;

        ImageFolder newFolder = datas.get(position);
        ImageFolder oldFolder = datas.get(checkPosition);

        oldFolder.setChecked(false);
        newFolder.setChecked(true);
        notifyItemChanged(checkPosition);
        notifyItemChanged(position);

        checkPosition = position;

        RxBus.singleton().post(new FolderClickEvent(position, newFolder));

      }
    });
  }

  @Override public int getItemCount() {
    return datas == null ? 0 : datas.size();
  }

  public void setDismissListener(View.OnClickListener dismissListener) {
    this.dismissListener = dismissListener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView tvName;
    private ImageView ivPreView;
    private ImageView ivCheck;

    private ViewHolder(View itemView) {
      super(itemView);
      tvName = (TextView) itemView.findViewById(R.id.tv_album_name);
      ivPreView = (ImageView) itemView.findViewById(R.id.iv_preview);
      ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
    }

    private void bind(ImageFolder imageFolder) {
      tvName.setText(imageFolder.getName());
      String path = imageFolder.getImages().get(0).getPath();
      RxPickerManager.getInstance().display(ivPreView, path, imageWidth, imageWidth);
      ivCheck.setVisibility(imageFolder.isChecked() ? View.VISIBLE : View.GONE);
    }
  }
}
