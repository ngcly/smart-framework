package org.cly.bean;

import org.cly.util.CastUtil;

import java.util.Map;

/**
 * 请求参数对象
 * Created by chen on 2017/5/24.
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String,Object> paramMap){
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名称获取long 型参数
     */
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     */
    public Map<String,Object> getMap(){
        return paramMap;
    }
}
