package com.caimuhao.rxpicker.ui.fragment.mvp;

import android.content.Context;
import com.caimuhao.rxpicker.bean.ImageFolder;
import com.caimuhao.rxpicker.base.BasePresenter;
import com.caimuhao.rxpicker.base.BaseView;
import java.util.List;

/**
 * @author Smile
 * @time 2017/4/19  上午10:38
 * @desc ${TODD}
 */
public interface PickerFragmentContract {

  interface View extends BaseView {
    void showAllImage(List<ImageFolder> datas);
  }

  public abstract class Presenter extends BasePresenter<View> {
    public abstract void loadAllImage(Context context);
  }
}
