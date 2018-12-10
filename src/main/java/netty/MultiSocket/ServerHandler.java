package netty.MultiSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("【服务器]-" + channel.remoteAddress() + "加入聊天室");
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("【服务器]-" + channel.remoteAddress() + "离开聊天室");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush(channel.remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush(channel.remoteAddress() + "下线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        final Channel channel = channelHandlerContext.channel();
        for (Channel ch : channels) {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息： " + s);
            } else {
                ch.writeAndFlush("【自己】" + s);
            }
        }
    }
}
