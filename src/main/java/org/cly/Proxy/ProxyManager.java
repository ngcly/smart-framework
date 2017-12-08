package org.cly.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 *
 * @author chen
 * @date 2017-12-05 11:55
 */
public class ProxyManager {
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams
                    , MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList);
            }
        });
    }
}
