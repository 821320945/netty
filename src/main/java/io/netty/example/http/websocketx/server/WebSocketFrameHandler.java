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
package io.netty.example.http.websocketx.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin.com.Utils;
import util.ByteObjectUtil;

import java.util.Locale;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled


        //普通HTTP接入
        if(frame instanceof FullHttpRequest){
            // handleHttpRequest(ctx, (FullHttpRequest) frame);
        }else if(frame instanceof WebSocketFrame){ //websocket帧类型 已连接
            //BinaryWebSocketFrame CloseWebSocketFrame ContinuationWebSocketFrame
            //PingWebSocketFrame PongWebSocketFrame TextWebScoketFrame
            handlerWebSocketFrame(ctx, frame);
        }



        // else {
        //     String message = "unsupported frame type: " + frame.getClass().getName();
        //     throw new UnsupportedOperationException(message);
        // }
    }



    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame) {

        if (frame instanceof TextWebSocketFrame) {
            //文本传输
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        }else if (frame instanceof CloseWebSocketFrame) {
            // 判断是否关闭链路的指令
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
                    .retain());
        }else if(frame instanceof BinaryWebSocketFrame) {

            // 测试二进制数据接收
            System.out.println("二进制数据接收");
            // ByteBuf buf = frame.content();
            //
            // for (int i = 0; i < buf.capacity(); i++){
            //     byte b = buf.getByte(i);
            //     System.out.println("byte:"+b);
            // }
            //
            // byte[] bytes = new byte[frame.content().readableBytes()];
            // frame.content().readBytes(bytes);
            // Object obj = ByteObjectUtil.restore(bytes);



            System.out.println("The WebSocketFrame is BinaryWebSocketFrame");
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            byte[] by = new byte[frame.content().readableBytes()];
            binaryWebSocketFrame.content().readBytes(by);
            ByteBuf bytebuf = Unpooled.buffer();
            bytebuf.writeBytes(by);

            byte[]  bytes = util.Utils.getByteFromBuf(bytebuf);

            Object obj = ByteObjectUtil.restore(bytes);



            ctx.channel().writeAndFlush(new BinaryWebSocketFrame((Unpooled.wrappedBuffer(by))));
        }

    }



    // /**
    //  * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
    //  */
    // @Override
    // public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    //     // 移除
    //     ctx.fireChannelActive();
    //     System.out.println("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
    // }
}