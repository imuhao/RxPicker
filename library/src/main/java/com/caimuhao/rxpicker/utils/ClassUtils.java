package com.caimuhao.rxpicker.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by dafan on 2016/6/16 0016.
 */
public class ClassUtils {
  public static <T> T getT(Object o, int i) {
    try {
      return ((Class<T>) ((ParameterizedType) (o.getClass()
          .getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Class<?> forName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
