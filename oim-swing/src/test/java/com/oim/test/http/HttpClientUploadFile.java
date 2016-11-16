package com.oim.test.http;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * @author XiaHui
 * @date 2014年12月29日 下午5:32:15
 */
public class HttpClientUploadFile {

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			new Upload().start();
		}

	}

	static class Upload extends Thread {

		public void run() {
			long time = System.currentTimeMillis();
			for (int i = 0; i < 1; i++) {
				uploads();
			}
			System.out.println("耗时：" + (System.currentTimeMillis() - time));
		}

		public void uploads() {
			// http://119.134.251.103:8010
			// String targetURL =
			// "http://192.168.2.88:7001/file/do?si=2&cd=0001&brand=0&account=33121321&fileType=001";
			// // 指定URLservleturl

			String targetURL = "http://119.134.251.103:8010/file/do?si=2&cd=0001&brand=0&account=33121321&fileType=001"; // 指定URLservleturl
			PostMethod filePost = new PostMethod(targetURL);

			try {
				File targetFile1 = new File("00001.log");// 指定上传文件
				// File targetFile2 = new File("00002.log");// 指定上传文件

				Part[] parts = { new FilePart(targetFile1.getName(), targetFile1) };
				filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
				int status = client.executeMethod(filePost);
				String body = filePost.getResponseBodyAsString();
				System.out.println(body);
				if (status == HttpStatus.SC_OK) {
					// System.out.println("上传成功");// 上传成功
				} else {
					System.out.println("上传失败");// 上传失败
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				filePost.releaseConnection();
			}
		}
	}
}
