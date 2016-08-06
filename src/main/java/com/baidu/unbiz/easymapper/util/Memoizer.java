package com.baidu.unbiz.easymapper.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带有记忆功能的工具类，可以在runtime根据算子{@link Computable}进行计算并且缓存结果，线程安全。类似函数式编程中的记忆特性。
 *
 * @author zhangxu
 */
public class Memoizer<K, V> {

    private static Logger LOGGER = LoggerFactory.getLogger(Memoizer.class);

    /**
     * 缓存
     */
    private final ConcurrentHashMap<K, Future<V>> cache = new ConcurrentHashMap<K, Future<V>>();

    /**
     * 获取值
     *
     * @param arg key
     *
     * @return 值
     */
    public V get(K arg) {
        Future<V> future = cache.get(arg);
        if (future != null) {
            try {
                return future.get();
            } catch (Exception e) {
                cache.remove(arg);
            }
        }
        return null;
    }

    /**
     * 清除
     */
    public void clear() {
        cache.clear();
    }

    /**
     * 计算并获取值
     *
     * @param arg key
     * @param c   算子
     *
     * @return 值
     *
     * @throws InterruptedException
     */
    public V compute(final K arg, final Computable<K, V> c) throws InterruptedException {
        Future<V> future = cache.get(arg);
        if (future == null) {
            FutureTask<V> futureTask = new FutureTask<V>(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            });
            future = cache.putIfAbsent(arg, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        try {
            return future.get();
        } catch (Exception e) {
            LOGGER.error("Get computed result with error - " + e.getMessage(), e);
            cache.remove(arg);
            return null;
        }
    }
}
