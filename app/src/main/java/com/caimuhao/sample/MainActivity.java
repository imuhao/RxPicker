package com.caimuhao.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.caimuhao.rxpicker.RxPicker;
import com.caimuhao.rxpicker.bean.MediaItem;
import com.caimuhao.rxpicker.ui.view.DividerGridItemDecoration;
import java.util.List;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btnSingleImg;
  private Button btnMultiImg;

  private RecyclerView recyclerView;
  private PickerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnSingleImg = (Button) findViewById(R.id.btn_single_img);
    btnSingleImg.setOnClickListener(this);

    btnMultiImg = (Button) findViewById(R.id.btn_multi_img);
    btnMultiImg.setOnClickListener(this);

    adapter = new PickerAdapter();
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    recyclerView.setAdapter(adapter);
  }

  @Override public void onClick(View v) {
    if (btnSingleImg == v) {
      RxPicker.of()
          .single(true)
          .camera(true)
          .crop(true)
          .start(this)
          .subscribe(new Action1<List<MediaItem>>() {
            @Override public void call(List<MediaItem> datas) {
              adapter.setData(datas);
              String path = datas.get(0).getPath();
              Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }
          });
    } else if (btnMultiImg == v) {

      RxPicker.of()
          .single(false)
          .camera(true)
          .crop(true)
          .start(this)
          .subscribe(new Action1<List<MediaItem>>() {
            @Override public void call(List<MediaItem> datas) {
              adapter.setData(datas);
              String path = datas.get(0).getPath();
              Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }
          });
    }
  }
}
