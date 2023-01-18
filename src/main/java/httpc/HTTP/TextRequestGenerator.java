package httpc.HTTP;

import httpc.Utils.Config;

public class TextRequestGenerator extends RequestGenerator {
    private String entity;

    public TextRequestGenerator(Config config, String entity, String agentName) {
        super(config, agentName);
        this.entity = entity;
        adjustHeader();
    }

    private void adjustHeader() {
        updateHeader("CONTENT-LENGTH", String.valueOf(entity.length()));
    }

    @Override
    public String generate() {
        return getMethod() + " " + getPath() + " " + getHttpVersion() + "\r\n" + generateHeaderString() + "\r\n" + entity + "\r\n";
    }

}
