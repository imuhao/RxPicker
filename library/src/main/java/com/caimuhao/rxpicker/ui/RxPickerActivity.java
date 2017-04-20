package com.caimuhao.rxpicker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.ui.fragment.PickerFragment;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/**
 * @author Smile
 * @time 2017/4/18  下午6:24
 * @desc ${TODD}
 */
public class RxPickerActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picker);
    requestPermission();
  }

  private void requestPermission() {
    new RxPermissions(this).request("android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE").subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (!aBoolean) {
          Toast.makeText(RxPickerActivity.this, "获取权限失败!", Toast.LENGTH_SHORT).show();
          finish();
        } else {
          setupFragment();
        }
      }
    });
  }

  private void setupFragment() {
    PickerFragment fragment = (PickerFragment) getFragmentManager().findFragmentByTag(
        PickerFragment.class.getSimpleName());
    if (fragment == null) {
      fragment = PickerFragment.newInstance();
      getFragmentManager().beginTransaction()
          .replace(R.id.fl_container, fragment, PickerFragment.class.getSimpleName())
          .commitAllowingStateLoss();
    }
  }
}
