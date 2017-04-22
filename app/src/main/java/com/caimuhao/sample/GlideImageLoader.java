package com.caimuhao.sample;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.caimuhao.rxpicker.utils.RxPickerImageLoader;

/**
 * @author Smile
 * @time 2017/4/19  下午3:38
 * @desc ${TODD}
 */
public class GlideImageLoader implements RxPickerImageLoader {

  @Override public void display(ImageView imageView, String path, int width, int height) {
    Glide.with(imageView.getContext())
        .load(path)
        .error(R.drawable.ic_preview_image)
        .into(imageView);
  }
}
