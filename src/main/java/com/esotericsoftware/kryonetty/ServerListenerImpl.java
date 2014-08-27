package com.esotericsoftware.kryonetty;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.esotericsoftware.kryo.Kryo;
import java.net.InetSocketAddress;
import lombok.Getter;
import org.jboss.netty.handler.ssl.SslContext;

/**
 * An implementation of Server that uses the listener model as per KryoNet.
 *
 * @author Philip Whitehouse
 *
 */
public class ServerListenerImpl extends Server {

    private List<Listener> listeners;
    
    private Kryo kryo = new Kryo();

    @Getter
    private boolean compression = false;

    @Getter
    private EndpointRole role;

    @Getter
    private final InetSocketAddress connectionAddress;

    @Getter
    private SslContext sslContext;

    public ServerListenerImpl(InetSocketAddress bindAddress) {
        super(bindAddress);
        role = EndpointRole.SERVER;
        this.connectionAddress = bindAddress;
        listeners = new ArrayList<Listener>();
    }

    public ServerListenerImpl(InetSocketAddress bindAddress, boolean compression) {
        this(bindAddress);
        this.compression = compression;
    }

    public ServerListenerImpl(InetSocketAddress bindAddress, boolean compression, SslContext sslContext) {
        this(bindAddress,compression);
        this.sslContext = sslContext;
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    @Override
    public void connected(ChannelHandlerContext ctx) {
        for (Listener listener : listeners) {
            listener.connected(ctx);
        }
    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {
        for (Listener listener : listeners) {
            listener.disconnected(ctx);
        }
    }

    @Override
    public void received(ChannelHandlerContext ctx, Object object) {
        for (Listener listener : listeners) {
            listener.received(ctx, object);
        }
    }
    
    @Override
    public Kryo getKryo() {
        if (kryo == null) {
            kryo = new Kryo();
        }
        return kryo;
    }

}
