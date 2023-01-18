package httpc.HTTP;

import httpc.Utils.Config;

import java.util.HashMap;
import java.util.Map;

public abstract class RequestGenerator {
    private String ip;
    private String method;
    private String path;
    private Map<String, String> header;
    private String httpVersion;
    private String agentName;
    private static String defaultHTTPVersion = "HTTP/1.1";

    public RequestGenerator(Config config, String agentName) {
        this(config.getUrl(), config.isHttpGet()? "GET":"POST", config.getHeaderOptions(), agentName, defaultHTTPVersion);
    }

    public RequestGenerator(String url, String method, Map<String, String> header, String agentName, String httpVersion) {
        this.parseURL(url);
        this.method = method;
        this.header = header;
        this.agentName = agentName;
        this.httpVersion = httpVersion;
        adjustHeader();
    }

    private void adjustHeader() {
        if (header == null) header = new HashMap<>();
        header.put("HOST", ip);
        header.put("USER-AGENT", agentName);
    }

    private void parseURL(String url) {
        url = url.replace("http://", "");
        ip = url.substring(0, url.indexOf('/'));
        path = url.substring(url.indexOf('/'));
    }

    protected String generateHeaderString() {
        StringBuilder sb = new StringBuilder();
        for (String s: header.keySet()) {
            sb.append(s);
            sb.append(": ");
            sb.append(header.get(s));
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public String generate(){
        return null;
    }

    public void redirect(String redirectIp) {
        ip = redirectIp.replace("http://", "");
        ip = ip.strip();
        if (ip.endsWith("/")){
            ip = ip.substring(0, ip.length() - 1);
        }
        adjustHeader();
    }

    public String getIp() {
        return ip;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getAgentName() {
        return agentName;
    }

    protected void updateHeader(String key, String val) {
        header.put(key, val);
    }
}
