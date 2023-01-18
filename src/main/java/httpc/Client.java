package httpc;

import httpc.HTTP.*;
import httpc.Utils.Config;
import httpc.Utils.IOUtils;

public class Client {
    private IOUtils ioUtil;
    private Config config;
    private HTTP http;
    private static String defaultAgentName = "Concordia";

    public Client(Config config) {
        this.config = config;
        ioUtil = new IOUtils(config);
        http = new HTTP(ioUtil);
        ioUtil.output(ioUtil.toVerbose("* " + config.toString() + "\n"));
    }

    public void sendRequest() {
        RequestGenerator requestGenerator = null;
        if (config.isHttpGet()) {
            requestGenerator = new GetRequestGenerator(config, defaultAgentName);
        } else {
            requestGenerator = new TextRequestGenerator(config, ioUtil.getPostData(), defaultAgentName);
        }
        Response response = http.request(requestGenerator);
        while (response.isRedirected()) {
            ioUtil.output(ioUtil.toVerbose("===============================redirect==================================\n"));
            ioUtil.output(ioUtil.toVerbose("* URL relocated to " + response.getRedirectIp() + "\n"));
            requestGenerator.redirect(response.getRedirectIp());
            response = http.request(requestGenerator);
        }

        ioUtil.output(ioUtil.toVerbose("===============================output==================================\n"));
        ioUtil.output(response.getContent());
    }


}
