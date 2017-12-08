package org.cly.core;

import org.cly.helper.*;
import org.cly.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 * Created by chen on 2017/5/24.
 */
public class HelperLoader {
    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,BeanHelper.class, AopHelper.class,IocHelper.class,ControllerHelper.class
        };
        for(Class<?> cls: classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }

}
