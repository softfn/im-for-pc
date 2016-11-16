package com.oim.core.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author XiaHui
 * @date 2015年1月21日 上午11:17:47
 */
public class HttpClientHandler {

	public String post(String url, String data) throws Exception {

		PostMethod postMethod = new PostMethod(url);
		InputStream in = null;
		BufferedReader reader = null;
		StringBuilder text = new StringBuilder();
		try {
			postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			postMethod.addParameter("data", data);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(6000);
			client.executeMethod(postMethod);
			in = postMethod.getResponseBodyAsStream();
			reader = new BufferedReader(new InputStreamReader((in), "UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				text.append(line);
			}
//			body = postMethod.getResponseBodyAsString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			postMethod.releaseConnection();
		}
		return text.toString();
	}
}
