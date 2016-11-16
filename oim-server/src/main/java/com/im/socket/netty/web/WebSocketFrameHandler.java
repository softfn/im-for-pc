package com.im.socket.netty.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.im.common.util.SpringUtil;
import com.im.business.server.handler.ServerHandler;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.socket.netty.session.ChannelSession;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	protected final Logger logger = LogManager.getLogger(this.getClass());
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	ServerHandler serverHandler = SpringUtil.getBean(ServerHandler.class);
	ChannelSession nettySession;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		if (frame instanceof TextWebSocketFrame) {
			String message = ((TextWebSocketFrame) frame).text();
			serverHandler.onMessage(message, nettySession);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		nettySession = new ChannelSession(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		serverHandler.onClose(nettySession);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Head head=new Head();
		head.setTime(System.currentTimeMillis());
		Message message=new Message();
		message.setHead(head);
		nettySession.write(message);
		if(logger.isDebugEnabled()){
			logger.debug(nettySession.getKey()+"：空闲");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
