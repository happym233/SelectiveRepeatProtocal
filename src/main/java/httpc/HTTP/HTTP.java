package httpc.HTTP;

import SelectiveRepeat.ReceiverSocket;
import SelectiveRepeat.SenderSocket;
import httpc.Utils.Config;
import httpc.Utils.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HTTP {
    private IOUtils ioUtils;
    private String httpVersion;
    private static String defaultHTTPVersion = "HTTP/1.1";

    public HTTP() {
        ioUtils = new IOUtils();
        this.httpVersion = defaultHTTPVersion;
    }

    public HTTP(IOUtils ioUtils, String httpVersion) {
        this.ioUtils = ioUtils;
        this.httpVersion = httpVersion;
    }

    public HTTP(IOUtils ioUtils) {
        this.ioUtils = ioUtils;
        this.httpVersion = defaultHTTPVersion;
    }


    public Response request(RequestGenerator requestGenerator) {
        Charset utf8 = StandardCharsets.UTF_8;
        String ip = requestGenerator.getIp();
        InetSocketAddress address = new InetSocketAddress(ip, Config.getServerPort());
        Response response = null;
        String req = requestGenerator.generate();
        try  {
            ReceiverSocket receiverSocket = new ReceiverSocket(Config.getClientPort());
            ioUtils.output(ioUtils.toVerbose("* Request generated: \n" + req + "\n"));
            ioUtils.output(ioUtils.toVerbose("* Connecting to: \n \tip: " + ip + "\n \tport: " + Config.getServerPort() + "\n"));
            receiverSocket.connect(address, req);
            response = new Response(receiverSocket.read());
            ioUtils.output(ioUtils.toVerbose("* Server response: \n" + response.getResponse()) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
