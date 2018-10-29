package com.zunder.smart.netty.message;

import android.util.Log;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.Base64;
import com.zunder.smart.MyApplication;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author wangqian
 * @date 17/12/5 上午10:54
 */

@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<Serializable> {
    private final Charset charset;

    public MessageEncoder() {
        this(Charset.defaultCharset());
    }

    public MessageEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        } else {
            this.charset = charset;
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, List<Object> out) throws Exception {
        if (msg != null) {
            if (SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
                String result = Base64.decodeStr((String) msg);
                String str = NettyConstant.NETTY_REMESSAGE_STARTING + result + NettyConstant.NETTY_MESSAGE_ENDING;
                Log.e("code",str);
                out.add(Unpooled.copiedBuffer(str, this.charset));
            }else {
                String str = NettyConstant.NETTY_MESSAGE_STARTING + msg + NettyConstant.NETTY_MESSAGE_ENDING;
                Log.e("code",str);
                out.add(Unpooled.copiedBuffer(str, this.charset));
            }
        }
    }
}
