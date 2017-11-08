package com.liukang.study.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.liukang.study.constants.Constant;
import com.liukang.study.util.RsMap;
import com.amass.framework.util.Str;
/**
 * 会话管理工具类
 * @author LiGn
 *
 */
public class SessionUtil {
	public static final String USER_SESSION = Constant.CACHE_PREFIX + "user_session:";
	public static final String EPA_USER_SESSION = Constant.CACHE_PREFIX + "epaAccountSession:";
	public static final int SESSION_EXPIRE = PpUtil.getInt("SESSION_EXPIRE",60);
	public static final String SESSIONID = "sessionId";
	/**
	 * 缓存从redis服务器取到的用户信息并绑定到线程中共享，以节省不必要的每次都到redis服务器取
	 */
	//private static ThreadLocal<MngTbUser> pool = new ThreadLocal<MngTbUser>();
//	private static final Map<String,MngTbUser> pool = new HashMap<String, MngTbUser>();
	
	/**
	 * 判断是否拥有指定机构下的数据访问权限
	 * @param user 登陆用户信息
	 * @param ogr_id
	 * @return
	 */
//	public static boolean hasRoleOrg(MngTbUser user,Long ogr_id)
//	{
//		MngTbOrg org = user.getMngTbOrg();
//		if(org == null)
//			return false;
//		String parentIds = org.getId() + "," + org.getParentIds() + ",";
//		return Str.IsEmpty(parentIds) || parentIds.indexOf(ogr_id + ",") >= 0;
//	}
	
	/**
	 * 判断是否拥有指定网格下的数据访问权限
	 * @param user 登陆用户信息
	 * @param ogr_id
	 * @return
	 */
//	public static boolean hasRoleGrid(MngTbUser user,Long grid_id)
//	{
////		List<CmTbGrid> grids = user.getGrids();
////		for(CmTbGrid grid : grids)
////		{
////			if(grid.getId().equals(grid_id))
////			{
////				return true;
////			}
////		}
//		return true;
//	}
//	
//	public static void cleanLocalCache(String sessionid)
//	{
//		pool.remove(sessionid);
//	}
	
	/**
	 * 从缓存获取已登陆APP用户会话信息
	 * @param username 登陆帐号
	 * @return
	 */
//	public static MngTbUser getUserSession(String sessionid)
//	{
//		MngTbUser obj = pool.get(sessionid);
//		if(obj == null)
//		{
//			long start = System.currentTimeMillis();
//			Object object = JedisUtils.getObject(USER_SESSION+sessionid);
//			long end = System.currentTimeMillis();
//			AppLog.debug("取会话:" + sessionid + ";" + (end-start));
//			
//			if(object != null)
//			{
//				obj = (MngTbUser)object;
//				pool.put(sessionid,obj);
//			}
//		}
//		return obj;
//	}
	
	/**
	 * 将已登陆APP用户会话信息存入缓存
	 * @param model
	 */
//	public static void setUserSession(String sessionid,MngTbUser user)
//	{
//		JedisUtils.setObject(getUserSessionCacheKey(sessionid), user, SESSION_EXPIRE);
//	}
	
	public static void setExpres(String sessionid)
	{
		JedisUtils.expire(getUserSessionCacheKey(sessionid), SESSION_EXPIRE);
	}
	
	/**
	 * 清除指定用户的会话信息
	 * @param sessionid
	 */
	public static void cleanUserSession(String sessionid)
	{
		String key = getUserSessionCacheKey(sessionid);
		JedisUtils.del(key);
	}
	
	private static String getUserSessionCacheKey(String sessionid)
	{
		return USER_SESSION+sessionid;
	}
	
	/**
	 * 读取请求输入数据，用于页面传JSON的时候，将输入参数自动转换成对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static RsMap getRequestParamMap(HttpServletRequest request) throws Exception
	{
		String contentType = request.getContentType();
		RsMap map = null;
		if(Str.IsEmpty(contentType))
		{
			return map;
		}else if(contentType.startsWith("application/json"))
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while((str = reader.readLine()) != null)
			{
				sb.append(str);
			}
			
			map = JsonUtil.getObject(sb.toString(), RsMap.class);
		}
		else
		{
			map = new RsMap();
			 Enumeration<?> attrs = request.getParameterNames();
	         String attr = null;
	         while(attrs.hasMoreElements())
	         {
	             attr = attrs.nextElement()+"";
	             map.put(attr, request.getParameter(attr));
	         }
	         if(map.isEmpty())
	         {
	        	if(ServletFileUpload.isMultipartContent(request))
	        	{
	        		FileItemFactory factory = new DiskFileItemFactory();
		    		ServletFileUpload upload = new ServletFileUpload(factory);
		    		upload.setHeaderEncoding("UTF-8");// UTF-8编码格式
		    		List<?> items = upload.parseRequest(request);
		    		Iterator<?> itr = items.iterator();
		    		
		    		while (itr.hasNext()) {
		    			FileItem item = (FileItem) itr.next();
		    			
		    			if (!item.isFormField()) { 
		    			} else
		    			{
		    				String fieldName = item.getFieldName();
		    				map.put(fieldName, item.getString("UTF-8"));
		    			}
		    		}
	        	}
	         }
		}
		return map;
	}
	
	/**
	 * 将App主密钥装入缓存
	 * @param sessionid 会话ID
	 * @param app_main_key 主密钥
	 */
	public static void putAppMainKey(String sessionid,String app_main_key)
	{
		String key = Constant.MKEY_KEY + sessionid;
		JedisUtils.set(key, app_main_key, 360);
	}
	
	/**
	 * 清除指定用户的主密钥缓存
	 * @param sessionid
	 */
	public static void cleanAppMainKey(String sessionid)
	{
		String key = Constant.MKEY_KEY + sessionid;
		JedisUtils.delObject(key);
	}
	
	/**
	 * 根据会话ID取APP主密钥
	 * @param sessionid
	 * @return
	 */
	public static String getAppMainKey(String sessionid)
	{
		String key = Constant.MKEY_KEY + sessionid;
		return JedisUtils.get(key);
	}
}
