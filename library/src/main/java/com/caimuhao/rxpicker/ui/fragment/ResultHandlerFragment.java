package com.caimuhao.rxpicker.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import com.caimuhao.rxpicker.bean.MediaItem;
import java.util.List;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * @author Smile
 * @time 2017/4/18  下午6:38
 * @desc ${TODD}
 */
public class ResultHandlerFragment extends Fragment {

  PublishSubject<List<MediaItem>> resultSubject = PublishSubject.create();

  BehaviorSubject<Boolean> attachSubject = BehaviorSubject.create();

  public static ResultHandlerFragment newInstance() {
    return new ResultHandlerFragment();
  }

  public PublishSubject<List<MediaItem>> getResultSubject() {
    return resultSubject;
  }

  public BehaviorSubject<Boolean> getAttachSubject() {
    return attachSubject;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null) {
      List<MediaItem> list =
          (List<MediaItem>) data.getSerializableExtra(PickerFragment.MEDIA_RESULT);
      resultSubject.onNext(list);
    }
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    attachSubject.onNext(true);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    attachSubject.onNext(true);
  }
}
