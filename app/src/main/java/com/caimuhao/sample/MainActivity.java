package com.caimuhao.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.caimuhao.rxpicker.RxPicker;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.caimuhao.rxpicker.ui.view.DividerGridItemDecoration;
import java.util.List;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Toolbar toolbar;
  private TextView btnSingleImg;
  private TextView btnMultiImg;

  private RecyclerView recyclerView;
  private PickerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("RxPicker");
    btnSingleImg = (TextView) findViewById(R.id.btn_single_img);
    btnSingleImg.setOnClickListener(this);

    btnMultiImg = (TextView) findViewById(R.id.btn_multi_img);
    btnMultiImg.setOnClickListener(this);

    adapter = new PickerAdapter();
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    recyclerView.setAdapter(adapter);
  }

  @Override public void onClick(View v) {
    if (btnSingleImg == v) {
      RxPicker.of().start(this).subscribe(new Action1<List<ImageItem>>() {
        @Override public void call(List<ImageItem> images) {
          adapter.setData(images);
        }
      });
    } else if (btnMultiImg == v) {
      RxPicker.of()
          .single(false)
          .camera(true)
          .limit(3)
          .start(this)
          .subscribe(new Action1<List<ImageItem>>() {
            @Override public void call(List<ImageItem> images) {
              adapter.setData(images);
            }
          });
    }
  }
}
