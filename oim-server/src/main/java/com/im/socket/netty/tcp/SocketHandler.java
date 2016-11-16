package com.im.socket.netty.tcp;

import com.im.common.util.SpringUtil;
import com.im.business.server.handler.ServerHandler;
import com.im.socket.netty.session.ChannelSession;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SocketHandler extends SimpleChannelInboundHandler<Object> {
	
	ServerHandler serverHandler = SpringUtil.getBean(ServerHandler.class);
	
	ChannelSession nettySession;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) throws Exception {
    	if (frame instanceof String) {
			String message = ((String) frame);
			serverHandler.onMessage(message, nettySession);
		}
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    	nettySession = new ChannelSession(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved");
        serverHandler.onClose(nettySession);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
       // ctx.close();
    }
}
