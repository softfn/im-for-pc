/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
//import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.im.socket.Server;
import com.im.socket.netty.http.HttpServer;
import com.im.socket.netty.tcp.SocketServer;
import com.im.socket.netty.web.WebSocketServer;
import com.startup.ApplicationConfig;
import com.startup.WebConfig;

/***
 * @Configuration @PropertySource("file:${app.home}/app.properties") public
 *                class AppConfig {
 * @Autowired Environment env; }
 * 
 *            System.setProperty("app.home", "test"); java -jar
 *            -Dapp.home="/home/mkyon/test" example.jar
 * @author: XiaHui
 *
 */
public final class StartupServer {

	public static void main(String[] args) throws Exception {
		System.setProperty("log4j.configurationFile", "config/log4j2.xml");
		System.setProperty("log4j.configuration", "file:config/log4j.properties");

		InputStream in = new BufferedInputStream(new FileInputStream("config/setting/config.properties"));
		ResourceBundle rb = new PropertyResourceBundle(in);
		in.close();

		int httpPort = Integer.parseInt(rb.getString("http.port"));
		int tcpPort = Integer.parseInt(rb.getString("tcp.port"));
		int websocketPort = Integer.parseInt(rb.getString("websocket.port"));

		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

		context.setServletContext(servletContext);
		context.setServletConfig(servletConfig);
		context.register(ApplicationConfig.class, WebConfig.class);
		context.refresh();
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		dispatcherServlet.init(servletConfig);

		HttpServer httpServer = new HttpServer(dispatcherServlet);
		SocketServer socketServer = new SocketServer();
		WebSocketServer webSocketServer = new WebSocketServer();

		httpServer.setPort(httpPort);
		socketServer.setPort(tcpPort);
		webSocketServer.setPort(websocketPort);

		new StartThread(httpServer).start();
		new StartThread(socketServer).start();
		new StartThread(webSocketServer).start();
	}

	static class StartThread extends Thread {

		private Server server;

		public StartThread(Server server) {
			this.server = server;
		}

		public void run() {
			server.start();
		}
	}
}
