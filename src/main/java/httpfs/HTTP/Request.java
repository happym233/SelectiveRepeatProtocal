package httpfs.HTTP;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String httpMethod;
    private String url;
    private String httpVersion;
    private Map<String, String> map;
    private String entity;

    public boolean validate(String request) {
        String[] lines = request.split("\n");
        map = new HashMap<>();
        try {
            String[] settings = lines[0].split(" ");
            httpMethod = settings[0];
            url = settings[1];
            httpVersion = settings[2];
            int i = 1;
            for (i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) {
                    break;
                }
                String[] mapkv = lines[i].split(": ");
                map.put(mapkv[0].toUpperCase(), mapkv[1].toUpperCase());
            }
            StringBuilder sb = new StringBuilder();
            for (; i < lines.length; i++) {
                sb.append(lines[i]);
            }
            entity = sb.toString();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "httpMethod: " + httpMethod +
                ", url: " + url +
                ", httpVersion: " + httpVersion +
                ", \nheaders: \n" + mapToString() +
                ", \nentity: " + entity;
    }

    private String mapToString() {
        StringBuilder sb = new StringBuilder();
        for (String key: map.keySet()) {
            sb.append(key.strip() + ": " + map.get(key).strip() + "\n");
        }
        return sb.toString();
    }
}
