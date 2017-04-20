package com.caimuhao.rxpicker.utils;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Smile
 * @time 2017/4/19  上午11:39
 * @desc ${TODD}
 */
public class CameraHelper {

  private static File takeImageFile;

  public static void take(Fragment fragment, int requestCode) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
      takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
      takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
      Uri uri;
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        uri = FileProvider.getUriForFile(fragment.getActivity(), "com.caimuhao", takeImageFile);
      } else {
        uri = Uri.fromFile(takeImageFile);
      }
      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }
    fragment.startActivityForResult(takePictureIntent, requestCode);
  }

  public static File getTakeImageFile() {
    return takeImageFile;
  }

  public static File createFile(File folder, String prefix, String suffix) {
    if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
    String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
    return new File(folder, filename);
  }
}
