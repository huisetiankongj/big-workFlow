package com.it313.big.modules.workFlow.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class BeanUtils {

	private final static PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
	/**
	 * 两个bean拷贝
	 * @param src
	 * @param target
	 * @param ignore
	 */
	public static void copyProperties(Object src,Object target,String[] ignore){
		List<String> list = null;  
        if (ignore == null) {  
            list = new ArrayList<String>();  
        } else {  
            list = Arrays.asList(ignore);  
        }    
	    PropertyDescriptor[] descr = PropertyUtils.getPropertyDescriptors(target);  
	    for (int i = 0; i < descr.length; i++) {  
            PropertyDescriptor d = descr[i];  
            try {  
            	if (d.getWriteMethod() == null)  
                    continue;  
                String name =d.getName();
                if (list.contains(d.getName()))  
                	continue;
                Object value = PropertyUtils.getProperty(src, name);
                PropertyUtils.setProperty(target, name, value);  
            } catch (Exception e) {  
                throw new RuntimeException("属性名：" + d.getName() + " 在实体间拷贝时出错", e);  
            }  
        }  
		
	}
}
