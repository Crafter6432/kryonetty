package com.esotericsoftware.kryonetty;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Skeleton Kryo server implementation using Netty.
 *
 * @author Nathan Sweet
 */
public abstract class Server implements Endpoint {

    private ServerBootstrap bootstrap;
    private Channel channel;

    private final InetSocketAddress connectionSocket;

    public InetSocketAddress getConnectionSocket() {
        return connectionSocket;
    }

    public Server(InetSocketAddress bindAddress) {
        connectionSocket = bindAddress;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(threadPool, threadPool));
        bootstrap.setPipelineFactory(new KryoChannelPipelineFactory(this));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.reuseAddress", true);
        channel = bootstrap.bind(bindAddress);
    }

    public Server(int port) {
        this(new InetSocketAddress(port));
    }

    public void close() {
        channel.close();
        channel = null;
    }
}
