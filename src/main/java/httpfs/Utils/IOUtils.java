package httpfs.Utils;

public class IOUtils {
    Config config;

    public IOUtils() { this.config = new Config(); }
    public IOUtils(Config config) {
        this.config = config;
    }

    public void print(String str) {
        if (config.isVerbose()) {
            System.out.print(str);
        }
    }

    public void println(String str) {
        if (config.isVerbose()) {
            System.out.println(str);
        }
    }

    public String toVerbose(String str) {
        if (config.isVerbose()) {
            return str;
        }
        return "";
    }
}
