package com.caimuhao.rxpicker.ui.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Smile
 * @time 2017/4/19  上午9:30
 * @desc ${TODD}
 */
public abstract class BasePresenter<V extends BaseView> {

  public V view;
  protected CompositeSubscription compositeSubscription = new CompositeSubscription();

  protected void start() {

  }

  public void attachModelView(V v) {
    this.view = v;
    start();
  }

  public void detachView() {
    this.view = null;
    if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
      compositeSubscription.unsubscribe();
    }
  }

  public Subscription add(Subscription subscription) {
    compositeSubscription.add(subscription);
    return subscription;
  }



}