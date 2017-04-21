package com.caimuhao.rxpicker.utils;

import java.lang.reflect.ParameterizedType;

/**
 * @author Smile
 * @time 2017/4/19  上午11:39
 * @desc ${TODD}
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
