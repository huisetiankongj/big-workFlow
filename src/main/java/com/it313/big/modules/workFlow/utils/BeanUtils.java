package com.it313.big.modules.workFlow.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	@SuppressWarnings("unchecked")
	public static <T> T map2Bean(T t,Map<String, ?> map) throws Exception{  
	    Class<? extends Object> clazz = t.getClass();  
	    //实例化类  
		T entity = (T)clazz.newInstance();  
	    Set<String> keys = map.keySet();  
	    //变量map 赋值  
	    for(String key:keys){  
	        String fieldName = key;  
	        //判断是sql 还是hql返回的结果  
	        if(key.equals(key.toUpperCase())){  
	            //获取所有域变量  
	            Field[] fields = clazz.getDeclaredFields();  
	            for(Field field: fields){  
	                if(field.getName().toUpperCase().equals(key)) fieldName=field.getName();  
	                break;  
	            }  
	        }  
	        //设置赋值  
	        try {  
	            //参数的类型  clazz.getField(fieldName)  
	            Class<?> paramClass = clazz.getDeclaredField(fieldName).getType();  
	            //拼装set方法名称  
	            String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);  
	            //根据名称获取方法  
	            Method method = clazz.getMethod(methodName, paramClass);  
	            //调用invoke执行赋值  
	            method.invoke(entity, map.get(key));  
	        } catch (Exception e) {  
	            //NoSuchMethod  
	        }  
	    }  
	      
	    return entity;  
	}   
}
