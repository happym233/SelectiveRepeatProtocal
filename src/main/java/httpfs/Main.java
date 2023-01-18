package httpfs;

import httpfs.Utils.Config;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.parse(args);
        try {
            Server server = new Server(config);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
