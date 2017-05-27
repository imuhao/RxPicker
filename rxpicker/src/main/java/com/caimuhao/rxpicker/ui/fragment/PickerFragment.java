package com.caimuhao.rxpicker.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.bean.FolderClickEvent;
import com.caimuhao.rxpicker.bean.ImageFolder;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.caimuhao.rxpicker.ui.PreviewActivity;
import com.caimuhao.rxpicker.ui.adapter.PickerFragmentAdapter;
import com.caimuhao.rxpicker.ui.base.AbstractFragment;
import com.caimuhao.rxpicker.ui.fragment.mvp.PickerFragmentContract;
import com.caimuhao.rxpicker.ui.fragment.mvp.PickerFragmentPresenter;
import com.caimuhao.rxpicker.ui.view.DividerGridItemDecoration;
import com.caimuhao.rxpicker.ui.view.PopWindowManager;
import com.caimuhao.rxpicker.utils.CameraHelper;
import com.caimuhao.rxpicker.utils.DensityUtil;
import com.caimuhao.rxpicker.utils.PickerConfig;
import com.caimuhao.rxpicker.utils.RxBus;
import com.caimuhao.rxpicker.utils.RxPickerManager;
import com.caimuhao.rxpicker.utils.T;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Smile
 * @time 2017/4/19  上午9:30
 * @desc ${TODD}
 */
public class PickerFragment extends AbstractFragment<PickerFragmentPresenter>
    implements PickerFragmentContract.View, View.OnClickListener {

  public static final int DEFALUT_SPAN_COUNT = 3;
  public static final int CAMERA_REQUEST = 0x001;
  private static final int CAMERA_PERMISSION = 0x002;

  public static final String MEDIA_RESULT = "media_result";

  private TextView title;
  private RecyclerView recyclerView;
  private ImageView ivSelectPreview;
  private TextView tvSelectOk;
  private RelativeLayout rlBottom;

  private PickerFragmentAdapter adapter;
  private List<ImageFolder> allFolder;

  private PickerConfig config;
  private Disposable folderClicksubscribe;
  private Disposable imageItemsubscribe;

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
    initToolbar(view);
    initRecycler();
    initObservable();
    loadData();
  }

  private void initToolbar(View view) {
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.nav_top_bar);
    final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
    appCompatActivity.setSupportActionBar(toolbar);
    appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        appCompatActivity.onBackPressed();
      }
    });
  }

  private void initObservable() {

    folderClicksubscribe = RxBus.singleton()
        .toObservable(FolderClickEvent.class)
        .subscribe(new Consumer<FolderClickEvent>() {
          @Override
          public void accept(@io.reactivex.annotations.NonNull FolderClickEvent folderClickEvent)
              throws Exception {
            String folderName = folderClickEvent.getFolder().getName();
            title.setText(folderName);
            refreshData(allFolder.get(folderClickEvent.getPosition()));
          }
        });

    imageItemsubscribe =
        RxBus.singleton().toObservable(ImageItem.class).subscribe(new Consumer<ImageItem>() {
          @Override public void accept(@io.reactivex.annotations.NonNull ImageItem imageItem)
              throws Exception {
            ArrayList<ImageItem> data = new ArrayList<>();
            data.add(imageItem);
            handleResult(data);
          }
        });
  }

  private void loadData() {
    presenter.loadAllImage(getActivity());
  }

  private void refreshData(ImageFolder folder) {
    adapter.setData(folder.getImages());
    adapter.notifyDataSetChanged();
  }

  private void initPopWindow(List<ImageFolder> data) {
    PopWindowManager popWindowManager = new PopWindowManager();
    popWindowManager.init(title, data);
  }

  private void initRecycler() {

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), DEFALUT_SPAN_COUNT);
    recyclerView.setLayoutManager(layoutManager);

    final DividerGridItemDecoration decoration = new DividerGridItemDecoration(getActivity());
    Drawable divider = decoration.getDivider();
    int imageWidth = DensityUtil.getDeviceWidth(getActivity()) / DEFALUT_SPAN_COUNT
        + divider.getIntrinsicWidth() * DEFALUT_SPAN_COUNT - 1;

    adapter = new PickerFragmentAdapter(imageWidth);
    adapter.setCameraClickListener(new CameraClickListener());
    recyclerView.addItemDecoration(decoration);
    recyclerView.setAdapter(adapter);
    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onItemRangeChanged(int positionStart, int itemCount) {
        tvSelectOk.setText(getString(R.string.select_confim, adapter.getCheckImage().size(),
            config.getMaxValue()));
      }
    });

    tvSelectOk.setText(
        getString(R.string.select_confim, adapter.getCheckImage().size(), config.getMaxValue()));
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //take camera
    if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
      handleCameraResult();
    }
  }

  private void handleCameraResult() {
    File file = CameraHelper.getTakeImageFile();
    CameraHelper.scanPic(getActivity(), file);
    for (ImageFolder imageFolder : allFolder) {
      imageFolder.setChecked(false);
    }
    ImageFolder allImageFolder = allFolder.get(0);
    allImageFolder.setChecked(true);
    ImageItem item =
        new ImageItem(0, file.getAbsolutePath(), file.getName(), System.currentTimeMillis());
    allImageFolder.getImages().add(0, item);
    RxBus.singleton().post(new FolderClickEvent(0, allImageFolder));
  }

  private void handleResult(ArrayList<ImageItem> data) {
    Intent intent = new Intent();
    intent.putExtra(MEDIA_RESULT, data);
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().finish();
  }

  @Override public void showAllImage(List<ImageFolder> datas) {
    allFolder = datas;
    adapter.setData(datas.get(0).getImages());
    adapter.notifyDataSetChanged();
    initPopWindow(datas);
  }

  @Override public void onDestroy() {
    super.onDestroy();

    if (!folderClicksubscribe.isDisposed()) {
      folderClicksubscribe.dispose();
    }

    if (!imageItemsubscribe.isDisposed()) {
      imageItemsubscribe.dispose();
    }
  }

  @Override public void onClick(View v) {
    if (tvSelectOk == v) {
      int minValue = config.getMinValue();
      ArrayList<ImageItem> checkImage = adapter.getCheckImage();

      if (checkImage.size() < minValue) {
        T.show(getActivity(), getString(R.string.min_image, minValue));
        return;
      }

      handleResult(checkImage);
    } else if (ivSelectPreview == v) {
      ArrayList<ImageItem> checkImage = adapter.getCheckImage();
      if (checkImage.isEmpty()) {
        T.show(getActivity(), getString(R.string.select_one_image));
        return;
      }
      PreviewActivity.start(getActivity(), checkImage);
    }
  }

  @TargetApi(23) private void requestPermission() {
    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION);
    } else {
      takePictures();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_PERMISSION) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        takePictures();
      } else {
        T.show(getActivity(), getString(R.string.permissions_error));
      }
    }
  }

  private void takePictures() {
    CameraHelper.take(PickerFragment.this, CAMERA_REQUEST);
  }

  private class CameraClickListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      if (Build.VERSION.SDK_INT >= 23) {
        requestPermission();
      } else {
        takePictures();
      }
    }
  }
}
