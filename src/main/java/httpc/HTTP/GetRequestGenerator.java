package httpc.HTTP;

import httpc.Utils.Config;

public class GetRequestGenerator extends RequestGenerator {

    public GetRequestGenerator(Config config, String agentName) {
        super(config, agentName);
    }

    @Override
    public String generate() {
        return getMethod() + " " + getPath() + " " + getHttpVersion() + "\r\n" + generateHeaderString() + "\r\n\r\n";
    }

}
