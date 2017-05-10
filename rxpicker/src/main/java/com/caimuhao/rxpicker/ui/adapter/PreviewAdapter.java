package com.caimuhao.rxpicker.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import com.caimuhao.rxpicker.utils.RxPickerManager;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.caimuhao.rxpicker.utils.DensityUtil;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/20  上午11:39
 * @desc ${TODD}
 */
public class PreviewAdapter extends PagerAdapter {

  private List<ImageItem> data;

  public PreviewAdapter(List<ImageItem> data) {
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
    ImageItem imageItem = data.get(position);
    container.addView(imageView);
    int deviceWidth = DensityUtil.getDeviceWidth(container.getContext());
    RxPickerManager.getInstance()
        .display(imageView, imageItem.getPath(), deviceWidth, deviceWidth);
    return imageView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
