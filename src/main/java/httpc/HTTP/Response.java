package httpc.HTTP;

public class Response {
    private String code;
    private String header;
    private String content;
    private String response;
    private static String[] redirectCodes = {"301", "302", "303", "307", "308"};

    public Response(String string) {
        System.out.println(string);
        this.response = string;
        extractContent();
    }

    private void extractContent() {
        int index = response.indexOf("\r\n\r\n");
        this.content = response.substring(index + 4);
        this.header = response.substring(0, index);
        this.code = header.split(" ")[1];
    }

    public String getContent() {
        return content;
    }

    public String getResponse() {
        return response;
    }

    public String getCode() {
        return code;
    }

    public boolean isRedirected() {
        for (String s: redirectCodes) {
            if (code.equals(s)) return true;
        }
        return false;
    }

    public String getRedirectIp() {
        if (!isRedirected()) return null;
        String[] lines = header.split("\n");
        for (String line: lines) {
            if (line.toLowerCase().contains("location: ")) {
                return line.substring("location: ".length());
            }
        }
        return null;
    }
}
