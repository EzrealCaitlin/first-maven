package com.liukang.study.tools;

import com.liukang.study.util.JsonMapper;


public class JsonUtil {

    /**
     * 将对象转为Json字符串
     * 
     * @param 要转换的对象
     * @return 转换后的Json数据
     */
    public static String createJsonString(Object value)
    {
    	JsonMapper mapper = JsonMapper.getInstance();
    	return mapper.toJson(value);
    }
    
    /**
     * 完成从DTO到MODEL的转换
     * @param dto
     * @param model
     * @return
     */
    public static <T> T Dto2Model(Object dto,Class<T> cls)
    {
    	return getObject(createJsonString(dto), cls);
    }

    /**
     * Json数据是一个对象
     * 
     * @param <T>
     * @param jsonData
     *            是一个对象
     * @param cls
     * @return 返回单个对象 T指任意泛型
     */
    public static <T> T getObject(String jsonData, Class<T> cls)
    {
    	JsonMapper mapper = JsonMapper.getInstance();
    	return mapper.fromJson(jsonData, cls);
    }
}
