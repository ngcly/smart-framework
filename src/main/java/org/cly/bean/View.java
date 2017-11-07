package org.cly.bean;

import com.sun.org.apache.regexp.internal.RE;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 * Created by chen on 2017/5/24.
 */
public class View {

    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public View(String path){
        this.path = path;
        model = new HashMap<String,Object>();
    }

    public View addModel(String key, Object value){
        model.put(key,value);
        return this;
    }

    public String getPath(){
        return path;
    }

    public Map<String, Object> getModel(){
        return model;
    }
}
