package org.cly.Proxy;

/**
 * @author chen
 * @date 2017-12-05 11:32
 */
public interface Proxy {
    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
