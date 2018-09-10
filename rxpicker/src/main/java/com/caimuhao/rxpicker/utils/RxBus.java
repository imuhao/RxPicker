package com.caimuhao.rxpicker.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Smile
 * @time 2017/4/19  上午12:39
 * @desc ${TODD}
 */

public class RxBus {

  private final PublishSubject<Object> bus = PublishSubject.create();

  private static final RxBus BUS = new RxBus();

  public static RxBus singleton() {
    return BUS;
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }

  public void post(Object o) {
    bus.onNext(o);
  }

  public <T> Observable<T> toObservable(Class<T> eventType) {
    return bus.ofType(eventType);
  }
}
