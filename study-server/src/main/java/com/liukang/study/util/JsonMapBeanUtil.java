package com.liukang.study.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liukang.study.model.JsonMapBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonMapBeanUtil {
	private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyyMMddhhmmss").create();

	/**
	 * 将普通MAP转换成JsonMapBean
	 * 
	 * @param map
	 * @return
	 */
	public static JsonMapBean parse(Map<String, Object> map) {
		if (map == null)
			return null;
		JsonMapBean target = new JsonMapBean();
		for (String k : map.keySet()) {
			target.put(k, map.get(k));
		}
		return target;
	}

	/**
	 * 将普通MAP转换成JsonMapBean
	 * 
	 * @param list
	 * @return
	 */
	public static List<JsonMapBean> parse(List<Map<String, Object>> list) {
		if (list == null) {
			return null;
		}
		List<JsonMapBean> re = new ArrayList<JsonMapBean>();
		for (Map<String, Object> map : list) {
			re.add(parse(map));
		}
		return re;
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
