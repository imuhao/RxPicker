package com.caimuhao.rxpicker.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import com.caimuhao.rxpicker.RxPickerManager;
import com.caimuhao.rxpicker.bean.MediaItem;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/20  上午11:39
 * @desc ${TODD}
 */
public class VpPreViewAdapter extends PagerAdapter {

  private List<MediaItem> data;

  public VpPreViewAdapter(List<MediaItem> data) {
    this.data = data;
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    AppCompatImageView imageView = new AppCompatImageView(container.getContext());
    ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
    imageView.setLayoutParams(layoutParams);
    MediaItem mediaItem = data.get(position);
    container.addView(imageView);
    RxPickerManager.getInstance().display(imageView, mediaItem.getPath(), 100, 100);
    return imageView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
