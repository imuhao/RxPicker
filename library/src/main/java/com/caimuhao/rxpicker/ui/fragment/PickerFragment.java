package com.caimuhao.rxpicker.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.caimuhao.rxpicker.PickerConfig;
import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.RxPickerManager;
import com.caimuhao.rxpicker.bean.FolderClickEvent;
import com.caimuhao.rxpicker.bean.MediaFolder;
import com.caimuhao.rxpicker.bean.MediaItem;
import com.caimuhao.rxpicker.ui.PreviewActivity;
import com.caimuhao.rxpicker.ui.adapter.PickerFragmentAdapter;
import com.caimuhao.rxpicker.ui.fragment.mvp.PickerFragmentContract;
import com.caimuhao.rxpicker.ui.fragment.mvp.PickerFragmentPresenter;
import com.caimuhao.rxpicker.ui.view.DividerGridItemDecoration;
import com.caimuhao.rxpicker.ui.view.PopWindowManager;
import com.caimuhao.rxpicker.utils.CameraHelper;
import com.caimuhao.rxpicker.utils.DensityUtil;
import com.caimuhao.rxpicker.utils.RxBus;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author Smile
 * @time 2017/4/19  上午9:30
 * @desc ${TODD}
 */
public class PickerFragment extends AbstractFragment<PickerFragmentPresenter>
    implements PickerFragmentContract.View, View.OnClickListener {

  public static final int DEFALUT_SPANCount = 3;
  public static final int CAMERA_REQUEST = 0x001;
  public static final String MEDIA_RESULT = "media_result";

  private TextView title;
  private RecyclerView recyclerView;
  private ImageView ivSelectPreview;
  private TextView tvSelectOk;
  private RelativeLayout rlBottom;

  private PickerFragmentAdapter adapter;
  private List<MediaFolder> allFolder;

  private PickerConfig config;
  private Subscription folderSubscribe;
  private Subscription mediaItemSubscribe;

  public static PickerFragment newInstance() {
    return new PickerFragment();
  }

  @Override protected int getLayoutId() {
    return R.layout.fragment_picker;
  }

  @Override protected void initView(View view) {
    config = RxPickerManager.getInstance().getConfig();
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    title = (TextView) view.findViewById(R.id.title);
    ivSelectPreview = (ImageView) view.findViewById(R.id.iv_select_preview);
    ivSelectPreview.setOnClickListener(this);
    tvSelectOk = (TextView) view.findViewById(R.id.iv_select_ok);
    tvSelectOk.setOnClickListener(this);
    rlBottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
    rlBottom.setVisibility(config.isSingle() ? View.GONE : View.VISIBLE);

    initRecycler();
    initObservable();
    loadData();
  }

  private void initObservable() {
    folderSubscribe = RxBus.singleton()
        .toObservable(FolderClickEvent.class)
        .subscribe(new Action1<FolderClickEvent>() {
          @Override public void call(FolderClickEvent folderClickEvent) {
            String folderName = folderClickEvent.getFolder().getName();
            title.setText(folderName);
            refreshData(allFolder.get(folderClickEvent.getPosition()));
          }
        });

    mediaItemSubscribe =
        RxBus.singleton().toObservable(MediaItem.class).subscribe(new Action1<MediaItem>() {
          @Override public void call(MediaItem mediaItem) {
            ArrayList<MediaItem> data = new ArrayList<>();
            data.add(mediaItem);
            handleResult(data);
          }
        });
  }

  private void loadData() {
    presenter.loadAllImage(getActivity());
  }

  private void refreshData(MediaFolder folder) {
    adapter.setDatas(folder.getImages());
    adapter.notifyDataSetChanged();
  }

  private void initPopWindow(List<MediaFolder> data) {
    PopWindowManager popWindowManager = new PopWindowManager();
    popWindowManager.init(title, data);
  }

  private void initRecycler() {
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), DEFALUT_SPANCount);
    recyclerView.setLayoutManager(layoutManager);
    DividerGridItemDecoration decoration = new DividerGridItemDecoration(getActivity());
    Drawable divider = decoration.getDivider();
    int imageWidth = DensityUtil.getDeviceWidth(getActivity()) / DEFALUT_SPANCount
        + divider.getIntrinsicWidth() * DEFALUT_SPANCount - 1;
    adapter = new PickerFragmentAdapter(imageWidth);
    adapter.setCameraClickListener(new CameraClickListener());
    recyclerView.addItemDecoration(decoration);
    recyclerView.setAdapter(adapter);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //camera
    if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
      MediaItem item = new MediaItem();
      item.setPath(CameraHelper.getTakeImageFile().getAbsolutePath());
      ArrayList<MediaItem> list = new ArrayList<>();
      list.add(item);
      handleResult(list);
    }
  }

  private void handleResult(ArrayList<MediaItem> data) {
    Intent intent = new Intent();
    intent.putExtra(MEDIA_RESULT, data);
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().finish();
  }

  @Override public void showAllImage(List<MediaFolder> datas) {
    allFolder = datas;
    adapter.setDatas(datas.get(0).getImages());
    adapter.notifyDataSetChanged();
    initPopWindow(datas);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (!folderSubscribe.isUnsubscribed()) folderSubscribe.unsubscribe();
    if (!mediaItemSubscribe.isUnsubscribed()) mediaItemSubscribe.unsubscribe();
  }

  @Override public void onClick(View v) {
    if (tvSelectOk == v) {
      ArrayList<MediaItem> checkImage = adapter.getCheckImage();
      handleResult(checkImage);
    } else if (ivSelectPreview == v) {
      ArrayList<MediaItem> checkImage = adapter.getCheckImage();
      if (checkImage.isEmpty()) {
        Toast.makeText(getActivity(), "请选择一张照片!", Toast.LENGTH_SHORT).show();
        return;
      }
      PreviewActivity.start(getActivity(), checkImage);
    }
  }

  private class CameraClickListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      CameraHelper.take(PickerFragment.this, CAMERA_REQUEST);
    }
  }
}
