package com.caimuhao.rxpicker.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Smile
 * @time 2017/4/19  上午9:30
 * @desc ${TODD}
 */
public abstract class BasePresenter<V extends BaseView> {

  public V view;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  protected void start() {

  }

  void attachModelView(V v) {
    this.view = v;
    start();
  }

  void detachView() {
    this.view = null;
    if (compositeDisposable != null && compositeDisposable.isDisposed()) {
      compositeDisposable.dispose();
    }
  }

  public Disposable add(Disposable disposable) {
    compositeDisposable.add(disposable);
    return disposable;
  }
}