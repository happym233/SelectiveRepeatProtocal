package httpfs.HTTP;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static String httpVersion = "HTTP/1.1";
    private static Map<Integer, String> messages;
    private int statusCode;
    private Map<String, String> header;
    private String entity;

    public Response(int statusCode, Map<String, String> headerMap, String entity) {
        messages = new HashMap<>();
        messages.put(200, "OK");
        messages.put(404, "NOT FOUND");
        messages.put(405, "METHOD NOT ALLOWED");
        messages.put(500, "INTERNAL SERVER ERROR");
        messages.put(400, "BAD REQUEST");

        this.header = headerMap;
        if (header == null) {
            this.header = new HashMap<>();
        }
        if (!header.containsKey("Content-Type")) {
            header.put("Content-Type", "text/html");
        }
        if (!header.containsKey("Content-Length")) {
            header.put("Content-Length", String.valueOf(entity.length()));
        }
        if (!header.containsKey("Content-Disposition")) {
            header.put("Content-Disposition", "inline");
        }
        header.put("Server", "mjt/0.1");
        this.statusCode = statusCode;
        this.entity = entity;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion + " " + statusCode + " " + messages.get(statusCode) + "\n");
        sb.append("Date: ");
        sb.append(Calendar.getInstance().getTime().toString() + "\n");
        sb.append(headerToStr());
        sb.append("\r\n\r\n");
        sb.append(entity);
        return sb.toString();
    }

    private String headerToStr() {
        StringBuilder sb = new StringBuilder();
        for (String k: header.keySet()) {
            sb.append(k + ": " + header.get(k) + "\n");
        }
        return sb.toString();
    }
}
