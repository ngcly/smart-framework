package org.cly.bean;

/**
 * 返回数据对象
 * Created by chen on 2017/5/24.
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model){
        this.model = model;
    }

    public Object getModel(){
        return model;
    }
}
