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
 * An implementation of Client that uses the listener model as per KryoNet.
 *
 * @author Philip Whitehouse
 *
 */
public class ClientListenerImpl extends Client {

    private List<Listener> listeners;

    private Kryo kryo;

    @Getter
    private boolean compression = false;

    @Getter
    private EndpointRole role;

    @Getter
    private final InetSocketAddress connectionAddress;

    @Getter
    private SslContext sslContext;

    public ClientListenerImpl(InetSocketAddress serverAddress) {
        super(serverAddress);
        role = EndpointRole.CLIENT;
        this.connectionAddress = serverAddress;
        listeners = new ArrayList<Listener>();
    }

    public ClientListenerImpl(InetSocketAddress serverAddress, boolean compression) {
        this(serverAddress);
        this.compression = compression;
    }

    public ClientListenerImpl(InetSocketAddress serverAddress, boolean compression, SslContext sslContext) {
        this(serverAddress, compression);
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
