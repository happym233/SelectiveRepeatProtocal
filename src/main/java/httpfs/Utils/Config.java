package httpfs.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config {
    private boolean verbose;
    private int port;
    private String pathToDir;
    private static int defaultPort = 8080;
    private static String defaultPath = "/";

    public Config() {
        initialize();
    }

    public Config(String[] args) {
        this();
        parse(args);
    }

    public void initialize() {
        verbose = false;
        port = defaultPort;
        pathToDir = defaultPath;
    }

    private String helpStr() {
        return "Usage: httpfs [-v] [-p PORT] [-d PATH-TO-DIR]";
    }

    private void throwException(String description) {
        throw new IllegalArgumentException(description + helpStr());
    }

    public void parse(String[] args) {
        int i = 0;
        Set<String> optionSet = new HashSet<>(Arrays.asList("-v", "-p", "-d"));
        while (i < args.length) {
            if (args[i].startsWith("-")) {
                if (!optionSet.contains(args[i])) {
                    throwException("Unknown option: " + args[i] + ". ");
                } else {
                    char c = args[i].charAt(1);
                    switch (c) {
                        case 'v':
                            verbose = true;
                            break;
                        case 'p':
                            String portStr = args[++i];
                            try {
                                port = Integer.valueOf(portStr);
                            } catch (Exception e) {
                                throwException("Invalid input " + portStr + " for port. ");
                            }
                            break;
                        case 'd':
                            pathToDir = args[++i];
                            break;
                    }
                }
                i++;
            } else {
                throwException("Unknown argument: " + args[i] + ". ");
            }
        }
        if (i < args.length) {
            throwException("Unknown command after " + args[i] + ". ");
        }
    }

    public boolean isVerbose() {
        return verbose;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPathToDir() {
        return pathToDir;
    }

    public void setPathToDir(String pathToDir) {
        this.pathToDir = pathToDir;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Option settings: \n");
        if (verbose) sb.append("Verbose: true, ");
        sb.append("port: " + port + ", ");
        sb.append("path: \"" + pathToDir + "\".");
        return sb.toString();
    }
}
