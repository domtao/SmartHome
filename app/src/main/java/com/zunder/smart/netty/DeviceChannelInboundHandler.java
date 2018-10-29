package com.zunder.smart.netty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zunder.smart.MyApplication;
import com.zunder.smart.utils.Base64;
import com.zunder.smart.MyApplication;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 设备处理响应handler
 */
@ChannelHandler.Sharable
public class DeviceChannelInboundHandler extends
		SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof String) {
			String resultStr=(String)msg;
			if (!resultStr.contains("{") || !resultStr.contains("}")) {
				resultStr = Base64.decodeStr(resultStr.trim());
			}
				Intent intent = new Intent("com.zunder.smart.receiver");
				Bundle bundle = new Bundle();
				bundle.putString("str", resultStr);
				intent.putExtras(bundle);
				MyApplication.getInstance().sendBroadcast(intent);
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
//		logger.error("{} exceptionCaught:", ctx.channel(), cause);
		ctx.channel().close();

		Log.e("netCode","exceptionCaught");
		MockLoginNettyClient.getInstans().isLinkFlag=5;
		MockLoginNettyClient.getInstans().plusNumber=0;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
//		logger.info("{} channelUnregistered", ctx.channel());
//		ctx.channel().close();
		Log.e("netCode","channelUnregistered");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
//		logger.info("{} channelRegistered", ctx.channel());
//		ctx.channel().close();
		Log.e("netCode","channelRegistered");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		/**
		 * 客户端心跳处理
		 */
		Log.e("netCode","userEventTriggered");
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
//				logger.info("{} READER_IDLE 读超时", ctx.channel());
			} else if (event.state() == IdleState.WRITER_IDLE) {
//				logger.info("{} WRITER_IDLE 写超时", ctx.channel());
			} else if (event.state() == IdleState.ALL_IDLE) {
//				logger.info("{} ALL_IDLE 总超时", ctx.channel());
				ctx.disconnect();
			}
		} else {
			super.userEventTriggered(ctx, evt);
//			logger.info("{} userEventTriggered", ctx.channel());
		}
	}
}
