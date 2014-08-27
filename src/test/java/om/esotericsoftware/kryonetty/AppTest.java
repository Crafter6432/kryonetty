package om.esotericsoftware.kryonetty;

import com.esotericsoftware.kryonetty.ClientListenerImpl;
import com.esotericsoftware.kryonetty.Listener;
import com.esotericsoftware.kryonetty.ServerListenerImpl;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.SSLException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.ssl.SslContext;
import org.jboss.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jboss.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Unit test for simple App.
 */
@NoArgsConstructor
@ToString
public class AppTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
        data = testName;
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws InterruptedException, SSLException, CertificateException {
        InetSocketAddress serverAddr = new InetSocketAddress("127.0.0.1", 5363);
        boolean compression = true;
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        ServerListenerImpl server = new ServerListenerImpl(serverAddr, compression,sslCtx);

        assertNotNull(server.getKryo());
        server.getKryo().register(AppTest.class, 1);

        server.addListener(new Listener() {

            public void connected(ChannelHandlerContext ctx) {
                ctx.getChannel().write(new AppTest(("Hello Client!")));
                System.out.println("Client connected");
            }

            public void disconnected(ChannelHandlerContext ctx) {
                System.out.println("Client disconnected");
            }

            public void received(ChannelHandlerContext ctx, Object object) {
                assertTrue(object instanceof AppTest);
                assertEquals(((AppTest) object).data, "Hello World");
                System.out.println(object);
            }
        });

        SslContext clientsslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);

        ClientListenerImpl client = new ClientListenerImpl(serverAddr, compression, clientsslCtx);

        assertNotNull(client.getKryo());

        client.addListener(new Listener() {

            public void connected(ChannelHandlerContext ctx) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void disconnected(ChannelHandlerContext ctx) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void received(ChannelHandlerContext ctx, Object object) {
                assertTrue(object instanceof AppTest);
                assertEquals(((AppTest) object).data, "Hello Client!");
                System.out.println(object);
            }
        });

        client.getKryo().register(AppTest.class, 1);
        Thread.sleep(100L);
        client.send(new AppTest("Hello World"));

        Thread.sleep(1000L);
        client.close();
        server.close();
    }

    public String data;

}
