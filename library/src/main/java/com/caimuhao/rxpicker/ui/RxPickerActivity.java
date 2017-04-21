package com.caimuhao.rxpicker.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.caimuhao.rxpicker.R;
import com.caimuhao.rxpicker.ui.fragment.PickerFragment;

/**
 * @author Smile
 * @time 2017/4/18  下午6:24
 * @desc ${TODD}
 */
public class RxPickerActivity extends AppCompatActivity {

  private static final int READ_STORAGE_PERMISSION = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picker);
    requestPermission();
  }

  private void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(RxPickerActivity.this, new String[] {
          Manifest.permission.READ_EXTERNAL_STORAGE
      }, READ_STORAGE_PERMISSION);
    } else {
      setupFragment();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == READ_STORAGE_PERMISSION) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        setupFragment();
      } else {
        Toast.makeText(RxPickerActivity.this, "获取权限失败!", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
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
