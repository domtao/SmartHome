package com.zunder.smart.netty.message;

import android.util.Log;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author wangqian
 * @date 17/12/5 上午11:12
 */

@ChannelHandler.Sharable
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final Charset charset;

    public MessageDecoder() {
        this(Charset.defaultCharset());
    }

    public MessageDecoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        } else {
            this.charset = charset;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String str = msg.toString(this.charset);
        Log.e("code1",str);
        if(str != null) {
            if (str.startsWith(NettyConstant.NETTY_REMESSAGE_STARTING)) {
                str = str.substring(NettyConstant.NETTY_REMESSAGE_STARTING.length());
                out.add(str);
            }else if (str.startsWith(NettyConstant.NETTY_MESSAGE_STARTING)) {
                str = str.substring(NettyConstant.NETTY_MESSAGE_STARTING.length());
                out.add(str);
            }
        }
    }
}
