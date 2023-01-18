package httpc.Utils;

import java.util.*;

public class Config {
    private boolean httpGet; // false if post
    private boolean verbose;
    private Map<String, String> headerOptions;
    private String inlineData;
    private String filePath;
    private String url;
    private Map<String, String> arguments;
    private String outputFilePath;

    private static int clientPort = 41830;
    private static int serverPort = 8007;

    public Config() {
        initialize();
    }

    public Config(String[] args) {
        initialize();
        parse(args);
    }

    private void initialize() {
        httpGet = true;
        verbose = false;
        headerOptions = new HashMap<>();
        inlineData = null;
        filePath = null;
        url = null;
    }

    private void throwArgumentException(String description) {
        throw new IllegalArgumentException(description + "\n" +
                "Usage: httpc (get|post) [-v] (-h \"k:v\")* [-d inline_data] [-f file] url [-o output_file]");
    }


    public void parse(String[] args) {
        boolean httpMethodSet = false;
        int i = 0;
        while (args[i].equalsIgnoreCase("get") || args[i].equalsIgnoreCase("post")) {
            String method = args[i];
            if (httpMethodSet) {
                throwArgumentException("GET/POST option has been set.");
            } else {
                httpGet = method.equalsIgnoreCase("get");
                httpMethodSet = true;
            }
            i++;
        }
        Set<String> optionSet = new HashSet<>(Arrays.asList("-v", "-h", "-d", "-f"));
        while (i < args.length && args[i].charAt(0) == '-') {
            String option = args[i];
            if (!optionSet.contains(option.toLowerCase())) {
                throwArgumentException("Unknown option " + option);
            }
            if (!option.equals("-v") && (i + 1 == args.length || args[i + 1].charAt(0) == '-')) {
                throwArgumentException("Option " + option + " requires an argument.");
            }
            char c = option.charAt(1);
            switch (c) {
                case 'v':
                    verbose = true;
                    break;
                case 'h':
                    String[] kv = args[++i].split(":");
                    if (kv.length != 2) {
                        throwArgumentException("Invalid key value pair on -h Option");
                    } else {
                        headerOptions.put(kv[0].toUpperCase(), kv[1].toUpperCase());
                    }
                    break;
                case 'd':
                    if (httpGet) {
                        throw new IllegalArgumentException("-d option is only used for POST method");
                    }
                    if (filePath != null) {
                        throw new IllegalArgumentException("Either [-d] or [-f] can be used but not both.");
                    }
                    String s = args[++i];
                    StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    if (s.startsWith("'")) {
                        while (!args[i].endsWith("'")) {
                            sb.append(args[++i]);
                        }
                    }
                    inlineData = sb.toString();
                    if (inlineData.length() > 1 && inlineData.startsWith("'") && inlineData.endsWith("'"))
                        inlineData = inlineData.substring(1, inlineData.length() - 1);
                    break;
                case 'f':
                    if (httpGet) {
                        throw new IllegalArgumentException("-d option is only used for POST method");
                    }
                    if (inlineData != null) {
                        throw new IllegalArgumentException("Either [-d] or [-f] can be used but not both.");
                    }
                    filePath = args[++i];
                    break;
            }
            i++;
        }

        if (i == args.length) {
            throwArgumentException("URL is required");
        } else {
            url = args[i];
            if (url.length() > 1 && url.startsWith("'") && url.endsWith("'"))
                url = url.substring(1, url.length() - 1);
            i++;
        }

        if (i < args.length && args[i].equals("-o")) {
            i++;
            if (i == args.length) throwArgumentException("Option " + args[i] + "requires an argument");
            else outputFilePath = args[i++];
        }

        if (i < args.length) throwArgumentException("Unknown commands after " + args[i]);
    }

    public boolean isHttpGet() {
        return httpGet;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public Map<String, String> getHeaderOptions() {
        return headerOptions;
    }

    public String getInlineData() {
        return inlineData;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUrl() {
        return url;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Option settings: \n");
        sb.append("http Method: " + ((httpGet)? "GET, ":"POST, "));
        if (verbose) sb.append("verbose: True, ");
        if (headerOptions.keySet().size() > 0) {
            sb.append("headers: ");
            for (String key: headerOptions.keySet()) {
                sb.append(key + ": " + headerOptions.get(key));
            }
        }
        if (inlineData != null) {
            sb.append(", inline data: " + inlineData + ", ");
        }
        if (filePath != null) {
            sb.append(", file name: " + filePath + ", ");
        }
        if (url != null) {
            sb.append("URL: " + url);
        }
        if (outputFilePath != null) {
            sb.append(", Output file path: " + outputFilePath);
        }
        return sb.toString();
    }

    private String mapPrint(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String k: map.keySet()) {
            sb.append("<");
            sb.append(k);
            sb.append(", ");
            sb.append(map.get(k));
            sb.append("> ");
        }
        return sb.toString();
    }

    public static int getClientPort() { return  clientPort; }
    public static int getServerPort() { return  serverPort; }
}
