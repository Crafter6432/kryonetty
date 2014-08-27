package com.esotericsoftware.kryonetty;

import com.esotericsoftware.kryo.Kryo;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.ssl.SslContext;

/**
 * @author Nathan Sweet
 */
public interface Endpoint {

    void connected(ChannelHandlerContext ctx);

    void disconnected(ChannelHandlerContext ctx);

    void received(ChannelHandlerContext ctx, Object object);

    Kryo getKryo();

    public boolean isCompression();

    public EndpointRole getRole();
    
    public InetSocketAddress getConnectionAddress();

    public SslContext getSslContext();

    public enum EndpointRole {

        SERVER, CLIENT;
    }
}
