package com.liukang.study.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.liukang.study.model.JsonMapBean;
import com.amass.framework.util.Function;
import com.amass.framework.util.Navigate;
import com.amass.framework.util.Str;

/**
 * 市商改平台接口调用工具类
 */
public class RequestHelper {
	public static Logger LOG = LoggerFactory.getLogger(RequestHelper.class);
	public static final String HOST = PropertiesUtil.getString("SGXT_HOST");
	public static final String CONTENT_TYPE_DEFAULT = "application/x-www-form-urlencoded; charset=UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	public static final String CONTENT_TYPE_MEDIA = "multipart/"+MediaType.MULTIPART_FORM_DATA+";charset=UTF-8";
	public static final String ACCEPT_CHARSET = PropertiesUtil.getString("SGXT_ACCEPT_CHARSET");
	public static final String UNIQUEMARK = PropertiesUtil.getString("SGXT_UNIQUEMARK");
	public static final int CONNECTIONTIMEOUT = PropertiesUtil.getInt("SGXT_CONNECTIONTIMEOUT");
	private static final Map<String, String> AUTHENTICATIONTOKEN = new HashMap<String, String>();

	// 是否使用代理访问网络
	private static final boolean USE_PROXY = PropertiesUtil.getInt("USE_PROXY", 0) == 1;

	// 代理服务器地址
	
	//config.properties  PROXY = 1 使用代理
	private static final String PROXY = PropertiesUtil.getString("PROXY", "192.168.1.251:8011");

	static final String ERROR_TIMEOUT = "201";

	/**
	 * 根据帐号，获取报文通讯所需校验令牌,并通过帐号作为键缓存起来
	 * 
	 * @param account
	 *            市商改平台的帐号
	 * @return 令牌数据AuthenticationToken的值
	 * @throws Exception
	 *             失败时抛出异常，失败原因在异常消息中
	 */
	private static String getAuthenticationToken(String account, boolean reload) throws Exception {
		String authenticationToken = AUTHENTICATIONTOKEN.get(account);
		if (Str.IsEmpty(authenticationToken) || reload) {
			// 当前帐号未登陆，进行登陆
			LOG.debug("认证用户:" + account);
			authenticationToken = login(account);
			AUTHENTICATIONTOKEN.put(account, authenticationToken);
		}
		return authenticationToken;
	}

	/**
	 * 登录市商改平台
	 * 
	 * @param account
	 * @return 成功时返回令牌数据AuthenticationToken的值
	 * @throws Exception
	 *             失败时抛出异常，失败原因在异常消息中
	 */
	private static String login(String account) throws Exception {
		String servletPath = HOST + "dgreform_serve/apiLogin/login";
		LOG.debug("访问的市商改平台URL:"+servletPath); 
		System.out.println("访问的市商改平台URL:"+servletPath);
		PostMethod postMethod = new PostMethod(servletPath);

		String authenticationToken = null;
		try {
			HttpClient client = new HttpClient();
			if (USE_PROXY) {
				String[] proxy = Function.split(PROXY, ":");
				client.getHostConfiguration().setProxy(proxy[0], Integer.parseInt(proxy[1]));
			}

			// 设置请求对象头信息
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ACCEPT_CHARSET);
			postMethod.setRequestHeader("Content-Type", CONTENT_TYPE_DEFAULT);
			postMethod.setRequestHeader("Accept-Charset", ACCEPT_CHARSET);
			postMethod.setParameter("account", account); // account:厚街商改
			postMethod.setParameter("uniqueMark", UNIQUEMARK); // uniqueMark:123456

			// 发送请求
			client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTIONTIMEOUT);
			int status = client.executeMethod(postMethod);

			if (status == HttpStatus.SC_OK) {
				String responseBody = "";
				JsonMapBean response = null;
				try {
					responseBody = new String(postMethod.getResponseBody(), ACCEPT_CHARSET);
					LOG.debug(responseBody);
					response = JsonMapBean.fromJson(responseBody);
					if (response == null) {
						throw new Exception("返回结果为空!");
					}
					if (!isSuccess(response)) {
						throw new Exception(findErrorText(response));
					}
				} catch (Exception e) {
					String str = "连接市商改平台失败[" + responseBody + "]";
					LOG.debug("URL:" + servletPath);
					LOG.debug("认证用户:" + account);
					LOG.error(str);
					throw new Exception(str);
				}

				if ("true".equals(response.getString("success"))) {
					authenticationToken = response.getString("data");
					if (Str.IsEmpty(authenticationToken)) {
						throw new Exception("连接市商改平台失败,登录返回令牌为空!");
					}
				} else {
					throw new Exception("连接市商改平台失败[message:" + response.getString("message") + "]");
				}

			} else if (status == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception("连接市商改平台失败[HttpStatus:" + status + "]，" + getStatusStr(status));
			}
		} finally {
			postMethod.releaseConnection();
		}
		return authenticationToken;
	}

	private static String getStatusStr(int status) {
		if (status == HttpStatus.SC_NOT_FOUND) {
			return "请求的页面不存在!";
		} else if (status == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			return "服务器出现异常!";
		} else if (status == HttpStatus.SC_NOT_IMPLEMENTED) {
			return "当前功能未实现!";
		} else if (status == HttpStatus.SC_BAD_GATEWAY) {
			return "连接代理服务器失败!";
		} else if (status == HttpStatus.SC_SERVICE_UNAVAILABLE) {
			return "服务器无法处理当前请求!";
		}
		return "";
	}

	/**
	 * 登陆接口
	 * 
	 * @param account
	 * @throws Exception
	 */
	public static void loginOut(String account) throws Exception {
		String authenticationToken = AUTHENTICATIONTOKEN.get(account);
		if (Str.IsEmpty(authenticationToken)) {
			return;
		}

		String servletPath = HOST + "dgreform_serve/apiLogin/loginOut";

		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(servletPath);
		try {
			if (USE_PROXY) {
				String[] proxy = Function.split(PROXY, ":");
				client.getHostConfiguration().setProxy(proxy[0], Integer.parseInt(proxy[1]));
			}
			client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTIONTIMEOUT);
			int status = client.executeMethod(postMethod);

			if (status != HttpStatus.SC_OK) {
				throw new Exception("连接市商改平台失败[HttpStatus:" + status + "]");
			}
		} catch (java.net.ConnectException e) {
			throw new Exception("连接市商改平台失败,连接不可用[" + e.getMessage() + "]");
		} catch (org.apache.commons.httpclient.ConnectTimeoutException e) {
			throw new Exception("连接市商改平台失败,连接超时");
		} finally {
			postMethod.releaseConnection();
		}

	}

	/**
	 * 全文检索证照信息接口
	 * 
	 * @param methodName
	 *            方法名，通常对应一个接口
	 * @param account
	 *            认证帐号，这里只能用部门的帐号
	 * @param request
	 *            请求参数对象封装
	 * @param navigate
	 *            分页器
	 * @see 东莞市商改平台接口说明文档v4.0.doc
	 * @return
	 * @throws Exception
	 */
	public static JsonMapBean lucene(String methodName, String account, JsonMapBean request, Navigate navigate)
			throws Exception {
		JsonMapBean response = null;
		String servletPath = HOST + "dgreform_serve/api/lucene/" + methodName;
		PostMethod method = new PostMethod(servletPath);
		JsonMapBean req = new JsonMapBean();
		if (navigate != null) {
			JsonMapBean limit = new JsonMapBean();
			limit.put("curPage", navigate.getPageNum());
			limit.put("pageSize", navigate.getPageSize());
			req.put("limit", limit);
		}

		req.put("searchMap", request);

		String data = req.toJson();
		LOG.debug("URL:" + servletPath);
		LOG.debug("data:" + data);
		StringRequestEntity entity = new StringRequestEntity(data, CONTENT_TYPE_JSON, ACCEPT_CHARSET);
		method.setRequestEntity(entity);

		try {
			response = doPost(method, account, CONTENT_TYPE_JSON);
		} catch (Exception e) {
			LOG.debug(servletPath);
			LOG.debug(data);
			LOG.error(e.getMessage());
			throw e;
		}
		if (!isSuccess(response)) {
			LOG.debug(servletPath);
			LOG.debug(data);
			LOG.error(findErrorText(response));
			throw new Exception(findErrorText(response));
		}
		if (navigate != null) {
			JsonMapBean limit = response.getBean("data").getBean("limit");
			navigate.setRowCount(limit.getInt("totalResults", 0));
		}

		return response;
	}

	public static JsonMapBean query(String methodName, String account, JsonMapBean request) throws Exception {
		JsonMapBean response = null;
		String servletPath = HOST + "dgreform_serve/api/query/" + methodName;
		PostMethod method = new PostMethod(servletPath);
		JsonMapBean req = new JsonMapBean();
		req.put("searchMap", request);

		String data = req.toJson();
		LOG.debug("URL:" + servletPath);
		LOG.debug("data:" + data);
		StringRequestEntity entity = new StringRequestEntity(data, CONTENT_TYPE_JSON, ACCEPT_CHARSET);
		method.setRequestEntity(entity);

		try {
			response = doPost(method, account, CONTENT_TYPE_JSON);
		} catch (Exception e) {
			LOG.debug(servletPath);
			LOG.debug(data);
			LOG.error(e.getMessage());
//			throw e;
		}
		if (!isSuccess(response)) {
			LOG.debug(servletPath);
			LOG.debug(data);
			LOG.error(findErrorText(response));
//			throw new Exception(findErrorText(response));
		}

		return response;
	}

	public static JsonMapBean submit(String methodName, String account, JsonMapBean request) throws Exception {
		JsonMapBean response = null;
		String servletPath = HOST + "dgreform_serve/api/submit/" + methodName;
		System.out.println("社区监管反馈市平台URL:"+servletPath);
		PostMethod method = new PostMethod(servletPath);

		JsonMapBean submitBean = new JsonMapBean();
		submitBean.put("datas", new JsonMapBean[] { request });

		String data = submitBean.toJson();
		LOG.debug("URL:" + servletPath);
		LOG.debug(data);
		StringRequestEntity stringRequestEntity = new StringRequestEntity(data, CONTENT_TYPE_JSON, ACCEPT_CHARSET);
		method.setRequestEntity(stringRequestEntity);
		
		response = doPost(method, account);

		// 报文通讯是否成功
		if (!isSuccess(response)) {
			LOG.debug(servletPath);
			LOG.debug(data);
			LOG.error(findErrorText(response));
			System.out.println("社区监管反馈市平台异常:"+findErrorText(response));
			throw new Exception(findErrorText(response));
		}

		// 提交结果判断
		JsonMapBean dataBean = response.getBean("data");
		if (dataBean.getInt("errorCount", 0) > 0) {
			List<JsonMapBean> errorBean = dataBean.getList("errorDatas");
			String msg = "请求失败，无明确失败原因";
			if (errorBean.size() > 0) {
				msg = errorBean.get(0).getString("errorMessage");
			}
			response.put("success", false);
			response.put("message", msg);
		}
		return response;
	}

	/**
	 * 调用市商改平台接口,如果发现未登录或登录超时，将自动发送登录报文并自动重发当前报文,
	 * 
	 * @param postMethod
	 *            此对象中应设置好请求跟径及请求参数
	 * @param account
	 *            登录帐号
	 * @param contentType
	 *            输入格式，默认为CONTENT_TYPE_JSON
	 * @return
	 * @throws Exception
	 */
	public static JsonMapBean doPost(PostMethod postMethod, String account, String... contentType) throws Exception {
		// 改为先验证
		String authenticationToken = getAuthenticationToken(account, false);
		// try
		// {
		// authenticationToken = getAuthenticationToken(account,false);
		// }catch (Exception e) {
		// throw new Exception("认证失败:["+e.getMessage()+"]");
		// }

		LOG.debug("account:" + account);
		LOG.debug("token:" + authenticationToken);
		LOG.debug("Accept-Charset:" + ACCEPT_CHARSET);
		if (contentType.length < 1) {
			postMethod.setRequestHeader("Content-Type", CONTENT_TYPE_JSON);
			LOG.debug("Content-Type:" + CONTENT_TYPE_JSON);
		} else {
			postMethod.setRequestHeader("Content-Type", contentType[0]);
			LOG.debug("Content-Type:" + contentType[0]);
		}
		postMethod.setRequestHeader("AuthenticationToken", authenticationToken);
		postMethod.setRequestHeader("Accept-Charset", ACCEPT_CHARSET);

		JsonMapBean response = null;
		response = invoke(postMethod, account);

		if ("false".equals(response.getString("success", "false"))) {
			String data = response.getString("data");
			if (Str.IsEmpty(data)) {
				throw new Exception("市商改平台返回数据错误:[" + response.getString("message") + "]");
			}
			if (data.length() == 3) {
				if (ERROR_TIMEOUT.equals(data)) {
					LOG.error("登陆超时，重新发起认证[" + account + "]");
					authenticationToken = getAuthenticationToken(account, true);
					LOG.debug("token:" + authenticationToken);
					postMethod.setRequestHeader("AuthenticationToken", authenticationToken);
					response = invoke(postMethod, account);
				}
			}
		}

		return response;
	}

	/**
	 * 调用市商改平台接口,如果发现未登录或登录超时，将自动发送登录报文并自动重发当前报文,
	 * 
	 * @param postMethod
	 *            此对象中应设置好请求跟径及请求参数
	 * @param account
	 *            登录帐号
	 * @param contentType
	 *            输入格式，默认为CONTENT_TYPE_JSON
	 * @return
	 * @throws Exception
	 */
	public static JsonMapBean doPost(HttpMethod postMethod, String account, String... contentType) throws Exception {
		// 改为先验证
		String authenticationToken = getAuthenticationToken(account, false);
		// try
		// {
		// authenticationToken = getAuthenticationToken(account,false);
		// }catch (Exception e) {
		// throw new Exception("认证失败:["+e.getMessage()+"]");
		// }

		LOG.debug("account:" + account);
		LOG.debug("token:" + authenticationToken);
		LOG.debug("Accept-Charset:" + ACCEPT_CHARSET);
		if (contentType.length < 1) {
			postMethod.setRequestHeader("Content-Type", CONTENT_TYPE_JSON);
			LOG.debug("Content-Type:" + CONTENT_TYPE_JSON);
		} else {
			postMethod.setRequestHeader("Content-Type", contentType[0]);
			LOG.debug("Content-Type:" + contentType[0]);
		}
		postMethod.setRequestHeader("AuthenticationToken", authenticationToken);
		postMethod.setRequestHeader("Accept-Charset", ACCEPT_CHARSET);

		JsonMapBean response = null;
		response = invoke(postMethod, account);

		if ("false".equals(response.getString("success", "false"))) {
			String data = response.getString("data");
			if (Str.IsEmpty(data)) {
				throw new Exception("市商改平台返回数据错误:[" + response.getString("message") + "]");
			}
			if (data.length() == 3) {
				if (ERROR_TIMEOUT.equals(data)) {
					LOG.error("登陆超时，重新发起认证[" + account + "]");
					authenticationToken = getAuthenticationToken(account, true);
					LOG.debug("token:" + authenticationToken);
					postMethod.setRequestHeader("AuthenticationToken", authenticationToken);
					response = invoke(postMethod, account);
				}
			}
		}

		return response;
	}

	/**
	 * 调用市商改平台接口,此为私有方法，不对外开放，外部调用应调用doPost方法以实现自动登陆及超时重发
	 * 
	 * @param postMethod
	 * @param account
	 * @return
	 * @throws Exception
	 */
	private static JsonMapBean invoke(PostMethod postMethod, String account) throws Exception {
		JsonMapBean response = null;
		HttpClient client = new HttpClient();
		try {
			client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTIONTIMEOUT);

			// 设置用代理访问
			if (USE_PROXY) {
				String[] proxy = Function.split(PROXY, ":");
				client.getHostConfiguration().setProxy(proxy[0], Integer.parseInt(proxy[1]));
			}

			int status = client.executeMethod(postMethod);
			String responseBody = "";

			InputStream is = postMethod.getResponseBodyAsStream();
			byte[] buf = new byte[0];
			byte[] temp = new byte[1024];
			int len = 0;
			int current = 0;
			while ((len = is.read(temp)) != -1) {
				current = buf.length;
				buf = Arrays.copyOf(buf, current + len);
				System.arraycopy(temp, 0, buf, current, len);
			}

			responseBody = new String(buf, ACCEPT_CHARSET);

			// responseBody = new
			// String(postMethod.getResponseBody(),ACCEPT_CHARSET);
			LOG.debug(responseBody);

			if (status == HttpStatus.SC_OK) {
				try {
					// responseBody = new
					// String(postMethod.getResponseBody(),ACCEPT_CHARSET);

					response = JsonMapBean.fromJson(responseBody);
					if (response == null) {
						throw new Exception("市商改平台无返回或数据解析失败");
					}
				} catch (Exception e) {
					throw new Exception("连接市商改平台失败,数据解析失败[" + responseBody + "]");
				}
			} else {
				throw new Exception("连接市商改平台失败[HttpStatus:" + status + "]:" + getStatusStr(status));
			}

		} catch (java.net.ConnectException e) {
			throw new Exception("连接市商改平台失败,连接不可用[" + e.getMessage() + "]");
		} catch (org.apache.commons.httpclient.ConnectTimeoutException e) {
			throw new Exception("连接市商改平台失败,连接超时");
		} finally {
			postMethod.releaseConnection();
		}
		return response;
	}

	/**
	 * 调用市商改平台接口,此为私有方法，不对外开放，外部调用应调用doPost方法以实现自动登陆及超时重发
	 * 
	 * @param postMethod
	 * @param account
	 * @return
	 * @throws Exception
	 */
	private static JsonMapBean invoke(HttpMethod postMethod, String account) throws Exception {
		JsonMapBean response = null;
		HttpClient client = new HttpClient();
		try {
			client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTIONTIMEOUT);

			// 设置用代理访问
			if (USE_PROXY) {
				String[] proxy = Function.split(PROXY, ":");
				client.getHostConfiguration().setProxy(proxy[0], Integer.parseInt(proxy[1]));
			}

			int status = client.executeMethod(postMethod);
			String responseBody = "";

			InputStream is = postMethod.getResponseBodyAsStream();
			byte[] buf = new byte[0];
			byte[] temp = new byte[1024];
			int len = 0;
			int current = 0;
			while ((len = is.read(temp)) != -1) {
				current = buf.length;
				buf = Arrays.copyOf(buf, current + len);
				System.arraycopy(temp, 0, buf, current, len);
			}

			responseBody = new String(buf, ACCEPT_CHARSET);

			// responseBody = new
			// String(postMethod.getResponseBody(),ACCEPT_CHARSET);
			LOG.debug(responseBody);

			if (status == HttpStatus.SC_OK) {
				try {
					// responseBody = new
					// String(postMethod.getResponseBody(),ACCEPT_CHARSET);

					response = JsonMapBean.fromJson(responseBody);
					if (response == null) {
						throw new Exception("市商改平台无返回或数据解析失败");
					}
				} catch (Exception e) {
					throw new Exception("连接市商改平台失败,数据解析失败[" + responseBody + "]");
				}
			} else {
				throw new Exception("连接市商改平台失败[HttpStatus:" + status + "]:" + getStatusStr(status));
			}

		} catch (java.net.ConnectException e) {
			throw new Exception("连接市商改平台失败,连接不可用[" + e.getMessage() + "]");
		} catch (org.apache.commons.httpclient.ConnectTimeoutException e) {
			throw new Exception("连接市商改平台失败,连接超时");
		} finally {
			postMethod.releaseConnection();
		}
		return response;
	}

	/**
	 * 错误代码表示的意思
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	private static String getErrorText(String errorCode) {
		int status = new Integer(errorCode);
		String text = null;
		switch (status) {
		case 101:
			text = "接口提交失败";
			break;
		case 102:
			text = "无任何提交数据";
			break;
		case 103:
			text = "超过最大允许提交数量";
			break;
		case 104:
			text = "无操作权限";
			break;
		case 201:
			text = "令牌过期";
			break;
		case 202:
			text = "链接错误";
			break;
		case 203:
			text = "登陆失败";
			break;
		case 204:
			text = "令牌为空";
			break;
		default:
			break;
		}
		return text;
	}

	/**
	 * 判断返回是否成功
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(JsonMapBean response) {
		if (response == null) {
			return false;
		}
		return "true".equals(response.getString("success", "false"));
	}

	/**
	 * 从返回结果中提取错误原因信息，调用此方法前请先调用isSuccess方法判断返回是否成功
	 * 
	 * @param response
	 * @return
	 */
	public static String findErrorText(JsonMapBean response) {
		String prefix = "市平台返回:";
		if (response == null) {
			return prefix + "未知错误!";
		}
		String text = response.getString("message");
		if (Str.IsEmpty(text)) {
			text = getErrorText(response.getString("data"));
			if (null == text) {
				text = response.getString("message", "未知错误!");
			}
		}

		return prefix + text;
	}

	public static List<JsonMapBean> getResults(JsonMapBean response) {
		List<JsonMapBean> list = null;
		String dataStr = response.getString("data");
		if (Str.IsEmpty(dataStr)) {
			list = new ArrayList<JsonMapBean>();
		} else {
			JsonMapBean dataSet = response.getBean("data");
			list = dataSet.getList("dataSet");
		}
		return list;
	}

	/***************************** 以下为业务具体方法 ******************************************/

	/**
	 * <b>获取未已分发信息接口</b><br />
	 * <div style="line-height:20px;text-indent:1em"> 获取责任监管部门一键分发后的信息，
	 * 每条信息已明确指定了监管单位 </div>
	 * <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据;
	 * 输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）即可获取到指定时间的数据。
	 * 此方法中默认取的是更新时间为两日前的监管任务 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇级部门、镇商改办、社区（村
	 * ）等账号登录后访问。市直部门获取的是本部门及其下属部门的数据；
	 * 镇商改办账号获取的是该镇所有社区（村）的数据；镇级部门与社区（村）获取的是本部门的数据 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request:
	 *            请求参数对象，可选值有：UPDATETIME(更新时间),QYZCH（企业注册号）,QYMC（企业名称）,HZRQ（核准日期
	 *            ）,TSSJ（推送时间）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean findPushData(String account, JsonMapBean request) throws Exception {
		return query("findPushData", account, request);
	}

	/**
	 * <b>获取已分发信息接口</b><br />
	 * <div style="line-height:20px;text-indent:1em"> 获取责任监管部门一键分发后的信息，
	 * 每条信息已明确指定了监管单位 </div>
	 * <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据;
	 * 输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）即可获取到指定时间的数据。
	 * 此方法中默认取的是更新时间为两日前的监管任务 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇级部门、镇商改办、社区（村
	 * ）等账号登录后访问。市直部门获取的是本部门及其下属部门的数据；
	 * 镇商改办账号获取的是该镇所有社区（村）的数据；镇级部门与社区（村）获取的是本部门的数据 </div>
	 * 
	 * @param account:
	 *            市商改帐号
	 * @param request:
	 *            请求参数对象，可选值有：UPDATETIME(更新时间),QYZCH（企业注册号）,QYMC（企业名称）,HZRQ（核准日期
	 *            ）,TSSJ（推送时间）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean findDistributeData(String account, JsonMapBean request) throws Exception {
		return query("findDistributeData", account, request);
	}

	/**
	 * <b>获取待处理异议接口</b><br />
	 * <div style="line-height:20px;text-indent:1em"> 市直部门或镇商改办通过该接口获取镇级部门或社区（村）
	 * 提起的异议。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据；输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）
	 * 即可获取到指定时间的数据。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口必须使用市直部门或镇商改办账号登录后才能访问。
	 * 市直部门获取的是其下属部门提起的异议；镇商改办账号获取的是该镇所有社区（村）提起的异议。 </div>
	 * 
	 * @param account:
	 *            市商改帐号
	 * @param request:
	 *            请求参数对象，可选值有：UPDATETIME(更新时间),QYZCH（企业注册号）,QYMC（企业名称）,HZRQ（核准日期
	 *            ）,TSSJ（推送时间）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean getObjectionData(String account, JsonMapBean request) throws Exception {
		return query("getObjectionData", account, request);
	}

	/**
	 * <b>获取监管反馈信息接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 市直部门或镇商改办通过该接口获取镇级部门或社区（村）
	 * 在商事登记改革协同监管系统中已反馈的监管结果 </div>
	 * <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据；
	 * 输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）即可获取到指定时间的数据。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇级部门、镇商改办、社区（村
	 * ）等账号登录后访问。市直部门获取的是本部门及其下属部门提交的反馈信息；
	 * 镇商改办账号获取的是该镇所有社区（村）提交的反馈信息；镇级部门与社区（村）获取的是本部门提交的反馈信息 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：UPDATETIME(更新时间)
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean getSsdjReply(String account, JsonMapBean request) throws Exception {
		return query("getSsdjReply", account, request);
	}

	/**
	 * <b>获取反馈待审核信息接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 市直部门通过该接口获取镇级部门反馈的监管结果。
	 * </div> <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据；输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）
	 * 即可获取到指定时间的数据。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口必须使用市直部门账号登录后才能访问，
	 * 获取的是其下属部门提交的监管反馈结果。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request:
	 *            请求参数对象，可选值有：UPDATETIME(更新时间),HZRQ（核准日期）,TSSJ（推送时间）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean getCheckData(String account, JsonMapBean request) throws Exception {
		return query("getCheckData", account, request);
	}

	/**
	 * <b>审核未通过的反馈信息接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 镇级部门通过该接口获取镇级部门反馈的监管结果。
	 * </div> <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是更新时间从上一个工作日开始到当前时间之间的数据；输入查询条件（如企业注册号、企业名称、核准日期、推送时间、更新时间等）
	 * 即可获取到指定时间的数据。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口必须使用镇级部门账号登录后才能访问，
	 * 获取的是本部门提交的但审核未通过的监管反馈信息。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request:
	 *            请求参数对象，可选值有：UPDATETIME(更新时间),HZRQ（核准日期）,TSSJ（推送时间）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean getNoCheckData(String account, JsonMapBean request) throws Exception {
		return query("getNoCheckData", account, request);
	}

	/**
	 * <b>监管单位调整接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 如果获取的商事登记后续监管信息指定的业务指导部门正确
	 * ，但指定的监管单位不正确时，可通过该接口调整未反馈信息的监管单位，可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口只能使用市直部门、镇商改办的账号登录后才能访问
	 * 。其中镇商改办只能调整本镇社区（村）的监管单位；市直部门只能调整本部门及其下属部门的监管单位。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：YWWYH(业务唯一号 必填),JGDW（监管单位
	 *            必填）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean adjustJgdw(String account, JsonMapBean request) throws Exception {
		return submit("adjustJgdw", account, request);
	}

	/**
	 * <b>提交信息分发结果接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 如果获取的商事登记后续监管信息是待分发信息，
	 * 则在提交监管反馈信息之前，必须调用该接口进行信息分发，可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口只能使用市直部门、镇商改办的账号登录后才能访问
	 * 。其中市直部门只能分发本部门及其下属部门的商事登记信息；镇商改办只能分发本镇社区（村）的商事登记信息。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：YWWYH(业务唯一号 必填),JGDW（监管单位
	 *            必填）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean distribute(String account, JsonMapBean request) throws Exception {
		return submit("distribute", account, request);
	}

	/**
	 * <b>提起异议接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em">
	 * 如果获取的商事登记后续监管信息指定的业务指导部门不正确时，可通过该接口他们向市市场监管办提起异议，异议提交成功后即可在本地删除该条信息，
	 * 可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇级部门、镇商改办或社区（村
	 * ）的账号登录后访问。市直部门可将当前监管单位为本部门或下属部门的信息向市市场监管办提起异议；镇商改办可将当前监管单位为本镇社区（村）
	 * 的信息向市市场监管办提起异议；镇级部门则将本部门监管的信息向其对应的市直部门提起异议。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：YWWYH(业务唯一号 必填),JGDW（监管单位
	 *            必填）,SM（异议原因 必填）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean objection(String account, JsonMapBean request) throws Exception {
		return submit("objection", account, request);
	}

	/**
	 * <b>监管反馈信息提交接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 提交监管反馈信息，可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇级部门、镇商改办或社区（村
	 * ）的账号登录后访问。市直部门可提交本部门及其下属部门的监管反馈信息；镇商改办可提交本镇的社区（村）的监管反馈信息；镇级部门与社区（村）
	 * 可提交本部门的监管反馈信息。 </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：QYZCH(企业注册号 必填),JGDW（监管单位
	 *            必填）,FKNR（监管结果 必填）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean feedback(String account, JsonMapBean request) throws Exception {
		return submit("feedback", account, request);
	}

	/**
	 * <b>审核反馈信息接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 当市直部门要求审核其下属部门提交的监管反馈信息时，
	 * 市直部门需要通过该接口获取其下属部门最近反馈的监管信息 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口只能使用市直部门的账号登录后访问。
	 * </div>
	 * 
	 * @param account:
	 *            市商改帐号 * @param request: 请求参数对象，可选值有：YWWYH(业务唯一号 必填),JGDW（监管单位
	 *            必填）,REPLY_CODE（反馈内容唯一标识 必填）,CHECK_SUCCESS（审核是否通过 必填
	 *            1-审核通过；0-审核未通过）
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean check(String account, JsonMapBean request) throws Exception {
		return submit("check", account, request);
	}

	/**
	 * <b>线索登记接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 提交线索登记信息，可通过该接口提交社区（村）
	 * 以及部门发现的线索，可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇商改办、镇级部门、社区（村
	 * ）的账号登录后访问。 </div>
	 * 
	 * @param account:
	 *            市商改帐号
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean registrationClues(String account, JsonMapBean request) throws Exception {
		return submit("registrationClues", account, request);
	}

	/**
	 * <b>获取线索接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 市直部门、
	 * 镇商改办或镇级部门通过该接口批量获取待处理的线索。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 获取数据时，如果不输入任何查询条件，
	 * 则默认获取的是线索接收时间从上一个工作日开始到当前时间之间的数据；输入线索接收时间即可获取到指定时间的数据。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口必须使用市直部门、
	 * 镇商改办或镇直部门账号登录后才能访问。市直部门获取的是其部门或下属部门相关的线索；镇商改办账号获取的是该镇所有社区（村）或本镇部门发起的线索；
	 * 镇直部门获取的是与其部门相关的线索。 </div>
	 * 
	 * * * @param request: 请求参数对象，可选值有：CLEWRECIVE_TIME(接收时间)
	 * 
	 * @param account:
	 *            市商改帐号
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean findCluesData(String account, JsonMapBean request) throws Exception {
		return query("findCluesData", account, request);
	}

	/**
	 * <b>线索处理接口</b><br />
	 * 
	 * <div style="line-height:20px;text-indent:1em"> 提交线索处理信息，
	 * 可通过该接口提交线索处理部门已处理的线索，可批量提交。 </div>
	 * <div style="line-height:20px;text-indent:1em"> 该接口可以使用市直部门、镇商改办、
	 * 镇级部门的账号登录后访问。 </div>
	 * 
	 * @param account:
	 *            市商改帐号
	 * @return JsonMapBean : 市商改返回结果，具体格式参考 @see 东莞市商改平台接口说明文档v2.0.doc
	 * @exception 任何错误及提示，均抛出异常，错误原因在异常message中
	 */
	public static JsonMapBean cluesHandle(String account, JsonMapBean request) throws Exception {
		return submit("cluesHandle", account, request);
	}

	public static JsonMapBean uploadFile(String account, File file) throws Exception {
		JsonMapBean response = null;
		String servletPath = HOST + "dgreform_serve/api/submit/uploadFile";
		MultipartPostMethod postMethod = new MultipartPostMethod(servletPath);
		Part part = new FilePart("multipartFile", file);
		LOG.debug("multipartFile:"+file.getName());
		postMethod.addPart(part);
		try{
			response = doPost(postMethod, account, CONTENT_TYPE_MEDIA);
		}catch(Exception e)
		{
			LOG.debug(servletPath);
//			LOG.debug(data);
			LOG.error(e.getMessage());
		}
		
		//报文通讯是否成功
		if(!isSuccess(response))
		{
			LOG.debug(servletPath);
			LOG.error(findErrorText(response));
			throw new Exception(findErrorText(response));
		}
		
		//提交结果判断
		JsonMapBean dataBean = response.getBean("data");
		if(dataBean.getInt("errorCount",0)>0)
		{
			List<JsonMapBean> errorBean = dataBean.getList("errorDatas");
			String msg = "请求失败，无明确失败原因";
			if(errorBean.size()>0)
			{
				msg = errorBean.get(0).getString("errorMessage");
			}
			response.put("success", false);
			response.put("message", msg);
		}
		return response;
	}
	
	public static JsonMapBean uploadFile(String account,String fileName) throws Exception{
		JsonMapBean response = null;
		String servletPath = HOST + "dgreform_serve/api/submit/uploadFile";
		MultipartPostMethod postMethod = new MultipartPostMethod(servletPath);
		LOG.debug("URL:" + servletPath);
		
		Part part = new FilePart("multipartFile", new File(fileName));
		LOG.debug("multipartFile:"+fileName);
		postMethod.addPart(part);
		try{
			response = doPost(postMethod, account, CONTENT_TYPE_MEDIA);
		}catch(Exception e)
		{
			LOG.debug(servletPath);
//			LOG.debug(data);
			LOG.error(e.getMessage());
		}
		
		//报文通讯是否成功
		if(!isSuccess(response))
		{
			LOG.debug(servletPath);
			LOG.debug("multipartFile:"+fileName);
			LOG.error(findErrorText(response));
			throw new Exception(findErrorText(response));
		}
		
		//提交结果判断
		JsonMapBean dataBean = response.getBean("data");
		if(dataBean.getInt("errorCount",0)>0)
		{
			List<JsonMapBean> errorBean = dataBean.getList("errorDatas");
			String msg = "请求失败，无明确失败原因";
			if(errorBean.size()>0)
			{
				msg = errorBean.get(0).getString("errorMessage");
			}
			response.put("success", false);
			response.put("message", msg);
		}
		return response;
	}
	
	/**
	 * 提交市场主体住所申报地图信息，可批量提交
	 * @param account
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JsonMapBean submitMap(String account,JsonMapBean request) throws Exception
	{
		return submit("submitMap", account, request);
	}
	
	/**
	 * 下载关键词
	 * @param account
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JsonMapBean findDeptKeyWord(String account,JsonMapBean request) throws Exception{
		return query("findDeptKeyWord", account, request);
	}
}
