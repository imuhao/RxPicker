package com.caimuhao.rxpicker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import com.caimuhao.rxpicker.bean.MediaItem;
import com.caimuhao.rxpicker.ui.RxPickerActivity;
import com.caimuhao.rxpicker.ui.fragment.ResultHandlerFragment;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Smile
 * @time 2017/4/18  下午6:01
 * @desc ${TODD}
 */
public class RxPicker {

  private RxPicker(PickerConfig config) {
    RxPickerManager.getInstance().setConfig(config);
  }

  /**
   * Using the custom config
   */

  public static RxPicker of(PickerConfig config) {
    return new RxPicker(config);
  }

  /**
   * Using the default config
   */
  public static RxPicker of() {
    return new RxPicker(new PickerConfig());
  }

  /**
   * Set the selection mode
   */
  public RxPicker single(boolean single) {
    RxPickerManager.getInstance()
        .setMode(single ? PickerConfig.Mode.SINGLE_IMG : PickerConfig.Mode.MULTIPLE_IMG);
    return this;
  }

  /**
   * Set the show  Taking pictures;
   */
  public RxPicker camera(boolean showCamera) {
    RxPickerManager.getInstance().showCamera(showCamera);
    return this;
  }

  /**
   * start picker from activity
   */
  public Observable<List<MediaItem>> start(Activity activity) {
    return start(activity.getFragmentManager());
  }

  /**
   * start picker from fragment
   */
  public Observable<List<MediaItem>> start(Fragment fragment) {
    return start(fragment.getFragmentManager());
  }

  private Observable<List<MediaItem>> start(FragmentManager fragmentManager) {
    ResultHandlerFragment fragment = (ResultHandlerFragment) fragmentManager.findFragmentByTag(
        ResultHandlerFragment.class.getSimpleName());

    if (fragment == null) {
      fragment = ResultHandlerFragment.newInstance();
      fragmentManager.beginTransaction()
          .add(fragment, fragment.getClass().getSimpleName())
          .commit();
    } else if (fragment.isDetached()) {
      fragmentManager.beginTransaction().attach(fragment).commit();
    }

    final ResultHandlerFragment finalFragment = fragment;
    return finalFragment.getAttachSubject().filter(new Func1<Boolean, Boolean>() {
      @Override public Boolean call(Boolean aBoolean) {
        return aBoolean;
      }
    }).flatMap(new Func1<Boolean, Observable<List<MediaItem>>>() {
      @Override public Observable<List<MediaItem>> call(Boolean aBoolean) {
        Intent intent = new Intent(finalFragment.getActivity(), RxPickerActivity.class);
        finalFragment.startActivityForResult(intent, ResultHandlerFragment.REQUEST_CODE);
        return finalFragment.getResultSubject();
      }
    }).take(1);
  }
}
