package com.liukang.study.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amass.framework.util.Str;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class JsonMapBean extends HashMap<String,Object> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonMapBean() {
		super();
	}

	/**
	 * 从map里面取一个字段串类型的值
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return getString(key, null);
	}

	/**
	 * 从map里面取一个字段串类型的值,不存在时用默认值代替
	 * 
	 * @param key
	 * @param defaultValue
	 *            不存在时的默认值
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		String re = get(key) + "";
		if (re == null || re.equals("null")) {
			re = defaultValue;
		}
		return re;
	}

	public Integer getInt(String key) {
		return getInt(key, 0);
	}

	public Integer getInt(String key, Integer defaultValue) {
		Object v = this.get(key);
		if (null == v) {
			return defaultValue;
		}
		if (Str.IsEmpty(v.toString())) {
			return defaultValue;
		}
		if (v instanceof String) {
			if (v.toString().indexOf(".") > 0) {
				return new Double(v.toString()).intValue();
			} else {
				return new Integer(v.toString());
			}
		}
		return v instanceof Double ? ((Double) v).intValue() : v instanceof Integer ? ((Integer) v) : defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		Object v = this.get(key);
		if (v == null) {
			return defaultValue;
		}
		return v instanceof Double ? ((Double) v).longValue()
				: v instanceof String ? new Long(v.toString()) : v instanceof Long ? ((Long) v) : defaultValue;
	}

	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	public Double getDouble(String key, Double defaultValue) {
		Object v = this.get(key);
		if (v == null) {
			return defaultValue;
		}
		return v instanceof Double ? ((Double) v) : v instanceof String ? new Double(v.toString()) : defaultValue;
	}

	/**
	 * 从参数对象中获取一个列表
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JsonMapBean> getList(String key) {
		Object obj = get(key);

		if (obj == null) {
			return new ArrayList<JsonMapBean>();
		}
		List<LinkedTreeMap> re = (List<LinkedTreeMap>) obj;
		List<JsonMapBean> list = new ArrayList<JsonMapBean>();
		for (LinkedTreeMap treeMap : re) {
			JsonMapBean target = new JsonMapBean();
			for (Object k : treeMap.keySet()) {
				target.put(k.toString(), treeMap.get(k));
			}
			list.add(target);
		}

		return list;
	}

	public JsonMapBean getBean(String key) {
		JsonMapBean re = new JsonMapBean();
		Object obj = get(key);
		if (obj == null) {
			return re;
		}
		if (obj instanceof LinkedTreeMap) {
			LinkedTreeMap treeMap = (LinkedTreeMap) obj;
			for (Object k : treeMap.keySet()) {
				re.put(k.toString(), treeMap.get(k));
			}
		}
		return re;
	}

	/**
	 * 从参数对象中获取一个列表(Map)
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JsonMapBean getDataSet(String key) {
		Object obj = get(key);

		if (obj == null) {
			return new JsonMapBean();
		}

		LinkedTreeMap<String, Object> re = (LinkedTreeMap<String, Object>) obj;
		JsonMapBean dataset = new JsonMapBean();
		for (Object k : re.keySet()) {
			JsonMapBean target = getDataSet(re.get(k));
			dataset.put(k.toString(), target);
		}

		return dataset;
	}

	@SuppressWarnings("unchecked")
	private JsonMapBean getDataSet(Object obj) {
		if (obj == null) {
			return new JsonMapBean();
		}

		LinkedTreeMap<String, Object> ltm = (LinkedTreeMap<String, Object>) obj;
		JsonMapBean target = new JsonMapBean();
		for (String k : ltm.keySet()) {
			target.put(k, ltm.get(k));
		}

		return target;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * 将对象转换成JSON数据格式的字符串
	 * 此默认格式下所有日期格式将自动转换成yyyyMMdd格式，如果一次请求中有多种类型的日期格式，请使用字符串的形式来传递
	 * 
	 * @return
	 */
	public String toJson() {
		return toJson("yyyyMMdd");
	}

	/**
	 * 将对象转换成JSON数据格式的字符串
	 * 
	 * @param dateFormat
	 *            : 如果参数中有日期格式，将自动转换成此格式的字符串,如:yyyyMMddhhmmss,yyyy-MM-dd
	 *            hh:mm:ss等
	 * @return
	 */
	public String toJson(String dateFormat) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat(dateFormat).create();
		return gson.toJson(this);
	}

	/**
	 * 将JSON格式字符串转换为JsonMapBean对象
	 * 
	 * @param jsonStr:格式数据
	 *            如:{"username":"administartor"}
	 * @return
	 */
	public static JsonMapBean fromJson(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, JsonMapBean.class);
	}

	public static void main(String[] args) {
		String json = "{ID:'22',\"data\":{\"RID\":1},'ROWS':[{'PHONE':'18688610020','NAME':'Liu','CERTNO':'440523198707300016','ROWNUMBER':1,'SEX':'男','ADDRESS':'Hk','ID':2,'ISDELETE':'0','IDCTYPE':'0','BORNDATE':'20150207'}],'TOTAL':1}";
		String json1 = "{\"KEYWORD\":{\"刘康\":\"你好,他好,我好\",\"小明\":\"好,好好,好好好\"}}";
		// json =
		// "{\"errorMessage\":null,\"message\":\"成功!\",\"errorType\":null,\"data\":{\"dataSet\":[{\"QYZT\":\"登记成立\",\"ZCZB\":1.2,\"YWZD_ORG\":\"厚街镇党政办\",\"LXDH\":\"15989906873\",\"XYDMBM\":\"5237\",\"YWWYH\":\"2015012914290026501108\",\"SSDJXX_ID\":\"d391522ee4974a4b8e9e972e848bf0bf\",\"TSZT\":\"tszt02\",\"HZRQ\":\"2015-01-26
		// 00:00:00\",\"FDDBR\":\"蔡维铿\",\"JGDW\":\"河田社区\",\"JYCSMJ\":60.0,\"GXDW\":\"东莞市工商行政管理局厚街分局\",\"ZS\":\"东莞市厚街镇河田村角元神仙水路7号\",\"FRSFZH\":\"445222198912031830\",\"CYRS\":1.0,\"ZCZBBZ\":\"人民币\",\"KEY_UNIQUE_ID\":\"d5f5e017d3b54f6ab5f1061c9e426f73\",\"XKJYBS\":\"1\",\"QYZCH\":\"441900606610649\",\"XYDM\":\"厨房用具及日用杂品零售\",\"YBJYXM\":\"零售：预包装食品、散装食品、乳制品(含婴幼儿配方乳粉)、酒类、卷烟、雪茄烟、日用品。\",\"PUSH_WAY\":\"tsfs02\",\"XYML\":\"F
		// \",\"TOWN_ORG_NAME\":\"厚街镇\",\"DJJG\":\"东莞市工商行政管理局\",\"CLRQ\":\"2014-11-20
		// 00:00:00\",\"TSSJ\":\"2015-01-29
		// 14:29:00\",\"QYMC\":\"东莞市厚街砂隆日用品店\",\"QYLX\":\"个体户\",\"FRZJLX\":\"1\"}],\"firstResult\":{\"QYZT\":\"登记成立\",\"ZCZB\":1.2,\"YWZD_ORG\":\"厚街镇党政办\",\"LXDH\":\"15989906873\",\"XYDMBM\":\"5237\",\"YWWYH\":\"2015012914290026501108\",\"SSDJXX_ID\":\"d391522ee4974a4b8e9e972e848bf0bf\",\"TSZT\":\"tszt02\",\"HZRQ\":\"2015-01-26
		// 00:00:00\",\"FDDBR\":\"蔡维铿\",\"JGDW\":\"河田社区\",\"JYCSMJ\":60.0,\"GXDW\":\"东莞市工商行政管理局厚街分局\",\"ZS\":\"东莞市厚街镇河田村角元神仙水路7号\",\"FRSFZH\":\"445222198912031830\",\"CYRS\":1.0,\"ZCZBBZ\":\"人民币\",\"KEY_UNIQUE_ID\":\"d5f5e017d3b54f6ab5f1061c9e426f73\",\"XKJYBS\":\"1\",\"QYZCH\":\"441900606610649\",\"XYDM\":\"厨房用具及日用杂品零售\",\"YBJYXM\":\"零售：预包装食品、散装食品、乳制品(含婴幼儿配方乳粉)、酒类、卷烟、雪茄烟、日用品。\",\"PUSH_WAY\":\"tsfs02\",\"XYML\":\"F
		// \",\"TOWN_ORG_NAME\":\"厚街镇\",\"DJJG\":\"东莞市工商行政管理局\",\"CLRQ\":\"2014-11-20
		// 00:00:00\",\"TSSJ\":\"2015-01-29
		// 14:29:00\",\"QYMC\":\"东莞市厚街砂隆日用品店\",\"QYLX\":\"个体户\",\"FRZJLX\":\"1\"},\"resultSize\":1.0,\"resultList\":[{\"QYZT\":\"登记成立\",\"ZCZB\":1.2,\"YWZD_ORG\":\"厚街镇党政办\",\"LXDH\":\"15989906873\",\"XYDMBM\":\"5237\",\"YWWYH\":\"2015012914290026501108\",\"SSDJXX_ID\":\"d391522ee4974a4b8e9e972e848bf0bf\",\"TSZT\":\"tszt02\",\"HZRQ\":\"2015-01-26
		// 00:00:00\",\"FDDBR\":\"蔡维铿\",\"JGDW\":\"河田社区\",\"JYCSMJ\":60.0,\"GXDW\":\"东莞市工商行政管理局厚街分局\",\"ZS\":\"东莞市厚街镇河田村角元神仙水路7号\",\"FRSFZH\":\"445222198912031830\",\"CYRS\":1.0,\"ZCZBBZ\":\"人民币\",\"KEY_UNIQUE_ID\":\"d5f5e017d3b54f6ab5f1061c9e426f73\",\"XKJYBS\":\"1\",\"QYZCH\":\"441900606610649\",\"XYDM\":\"厨房用具及日用杂品零售\",\"YBJYXM\":\"零售：预包装食品、散装食品、乳制品(含婴幼儿配方乳粉)、酒类、卷烟、雪茄烟、日用品。\",\"PUSH_WAY\":\"tsfs02\",\"XYML\":\"F
		// \",\"TOWN_ORG_NAME\":\"厚街镇\",\"DJJG\":\"东莞市工商行政管理局\",\"CLRQ\":\"2014-11-20
		// 00:00:00\",\"TSSJ\":\"2015-01-29
		// 14:29:00\",\"QYMC\":\"东莞市厚街砂隆日用品店\",\"QYLX\":\"个体户\",\"FRZJLX\":\"1\"}]},\"beanType\":\"JsonBean\",\"attributes\":{\"__system_time\":1.42742745189E12},\"success\":true}";
		JsonMapBean bean = JsonMapBean.fromJson(json1);
		System.out.println(bean.getInt("ID"));
		String keyword = bean.getString("KEYWORD");
		String keywords = "";
		String keywordDepts = "";
		if(!Str.IsEmpty(keyword)){
			//格式为 {xxx=yyy1,yyy2,yyy3, aaa=bbb1,bbb2,bbb3}
			System.out.println("############################"+keyword);
			//去掉前后的{}
			keyword=keyword.substring(1, keyword.length()-1);
			//解析数据
			String[] key = keyword.split("=");
			for(int i = 0; i < key.length; i++){
				if(i==0){
					keywordDepts+=key[i];
				}else if(i==key.length-1){
					keywords+=key[i];
				}else{
					keywords += key[i].substring(0, key[i].lastIndexOf(", "))+"|";
					keywordDepts +="|" + key[i].substring(key[i].lastIndexOf(", ")+2);
				}
			}
		}
		// System.out.println(bean.getList("ROWS").get(0).getString("PHONE"));
		System.out.println(bean.getBean("data").getList("dataSet").get(0).getString("QYZT"));
	}
}
