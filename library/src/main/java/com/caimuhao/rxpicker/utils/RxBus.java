
package com.caimuhao.rxpicker.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

  private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

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
