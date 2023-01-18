package httpfs;

import SelectiveRepeat.SenderSocket;
import httpfs.HTTP.RequestHandler;
import httpfs.Utils.Config;
import httpfs.Utils.FileHandler;
import httpfs.Utils.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private Thread mainThread;
    private ExecutorService threadPool;
    private FileHandler fileHandler;
    private RequestHandler requestHandler;
    private boolean stop;
    private IOUtils ioUtils;

    public Server(Config config) throws IOException {
        ioUtils = new IOUtils(config);
        this.port = config.getPort();
        try {
            this.fileHandler = new FileHandler(config.getPathToDir());
        } catch (IOException e) {
            throw new IOException("Unknown file path: " + config.getPathToDir());
        }
        requestHandler = new RequestHandler(fileHandler);
        ioUtils.println("* " + config.toString());
    }

    public void runServer() {
        try {
            SenderSocket ss = new SenderSocket(port);
            String request = ss.accept();
            ioUtils.println("* New connection from " + ss.getReceiverAddress().getAddress() + " port " + ss.getReceiverAddress().getPort());
            String response;
            ioUtils.println("=========================New Request========================");
            ioUtils.println("* Request from " + ss.getReceiverAddress().getAddress() + " port " + ss.getReceiverAddress().getPort() + ": ");
            ioUtils.println(request);
            response = requestHandler.handle(request);
            ioUtils.println("* Response to " + ss.getReceiverAddress().getAddress() + " port " + ss.getReceiverAddress().getPort() + ":");
            ioUtils.println(response);
            ioUtils.println("============================================================");
            ss.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
