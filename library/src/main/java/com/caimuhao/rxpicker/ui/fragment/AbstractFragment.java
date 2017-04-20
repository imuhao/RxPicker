package com.caimuhao.rxpicker.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.caimuhao.rxpicker.ui.base.BasePresenter;
import com.caimuhao.rxpicker.ui.base.BaseView;
import com.caimuhao.rxpicker.utils.ClassUtils;

/**
 * @author Smile
 * @time 2017/4/19  上午9:38
 * @desc ${TODD}
 */
public abstract class AbstractFragment<P extends BasePresenter> extends Fragment
    implements BaseView {

  public P presenter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    presenter = ClassUtils.getT(this, 0);
    presenter.attachModelView(this);
    initView(view);
    return view;
  }

  protected abstract int getLayoutId();

  protected abstract void initView(View view);

  public void showWaitDialog() {

  }

  public void hideWaitDialog() {

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (presenter != null) {
      presenter.detachView();
    }
  }
}
