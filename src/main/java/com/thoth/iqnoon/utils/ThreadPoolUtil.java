package com.thoth.iqnoon.utils;

import java.util.concurrent.*;

/**
 * @author: dengpeng chen
 * @date: 2019/11/1 10 : 06
 * @description:
 */
public class ThreadPoolUtil {

  private static ThreadPoolExecutor threadPool;

  private ThreadPoolUtil() {

  }

  /**
   *
   * @param callable
   */
  public  static <T> Future<T> submit(Callable<T> callable){
    return   getThreadPool().submit(callable);
  }


  /**
   *
   * @return 线程池对象
   */
  public static ThreadPoolExecutor getThreadPool() {
    if (threadPool != null) {
      return threadPool;
    } else {
      synchronized (ThreadPoolUtil.class) {
        if (threadPool == null) {
          threadPool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS,
              new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return threadPool;
      }
    }
  }

}
