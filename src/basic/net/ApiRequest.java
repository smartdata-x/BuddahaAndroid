package basic.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午7:33:59 类说明
 */
public class ApiRequest {

	public static String postRequest(String sURL, Map<String, String> params) {
		MyLog.v("ApiRequest", "sURL = " + sURL);
		MyLog.v("ApiRequest", "params = " + params);

		HttpClient httpclient = new DefaultHttpClient();
		// 超时请求
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);

		HttpPost httppost = new HttpPost(sURL);
		ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
				2);
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			try {
				String sParamKey = it.next();
				String sParam = params.get(sParamKey);
				nameValuePairs.add(new BasicNameValuePair(sParamKey, sParam));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));// 设置post参数 并设置编码格式
			HttpResponse response = httpclient.execute(httppost);

			String result = retrieveInputStream(response.getEntity());
			Header[] h = response.getAllHeaders();
			MyLog.v("" + h[0]);
			int statusCode = response.getStatusLine().getStatusCode();
			MyLog.v("statusCode:" + statusCode);

			return result;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getRequest(String url) throws Exception {
		return getRequest(url, new DefaultHttpClient(new BasicHttpParams()));
	}

	protected static String getRequest(String url, DefaultHttpClient client)
			throws Exception {
		String result = null;
		int statusCode = 0;
		HttpGet getMethod = new HttpGet(url);
		MyLog.d("getRequest url: " + url);

		try {
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					10000);

			HttpResponse httpResponse = client.execute(getMethod);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			result = retrieveInputStream(httpResponse.getEntity());
		} catch (SocketTimeoutException se) {
			throw se;
		} catch (Exception e) {
			throw e;
		} finally {
			getMethod.abort();
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		return result;
	}

	protected static String retrieveInputStream(HttpEntity httpEntity) {
		Long l = httpEntity.getContentLength();
		int length = (int) httpEntity.getContentLength();

		if (length <= 0)
			length = 10000;
		StringBuffer stringBuffer = new StringBuffer(length);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					httpEntity.getContent(), HTTP.UTF_8);
			char buffer[] = new char[length];
			int count;
			while ((count = inputStreamReader.read(buffer, 0, length)) > 0) {
				stringBuffer.append(buffer, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	public static String getText(String url) throws Exception {
		String result = null;
		HttpGet getMethod = new HttpGet(url);
		DefaultHttpClient client = null;
		MyLog.d("getText url: " + url);
		try {
			client = new DefaultHttpClient(new BasicHttpParams());
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					10000);

			HttpResponse httpResponse = client.execute(getMethod);
			// statusCode = httpResponse.getStatusLine().getStatusCode();
			result = readStream(httpResponse.getEntity());
		} catch (SocketTimeoutException se) {
			throw se;
		} catch (Exception e) {
			throw e;
		} finally {
			getMethod.abort();
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		return result;
	}

	protected static String readStream(HttpEntity httpEntity) {
		int length = (int) httpEntity.getContentLength();

		if (length <= 0)
			length = 10000;
		StringBuffer stringBuffer = new StringBuffer(length);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					httpEntity.getContent(), "gbk");
			char buffer[] = new char[length];
			int count;
			while ((count = inputStreamReader.read(buffer, 0, length)) > 0) {
				stringBuffer.append(buffer, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

}
