package httpc.Utils;

import java.io.*;

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

    public void output(String str) {
        if (config.getOutputFilePath() == null) {
            System.out.print(str);
        } else {
            try {
                File file = new File(config.getOutputFilePath());
                file.createNewFile();
                FileWriter writer = new FileWriter(file, true);
                writer.write(str);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPostData()  {
        if (config.getInlineData() != null) {
            output(toVerbose("*Content to post: " + config.getInlineData()));
            return config.getInlineData();
        } else if (config.getFilePath() != null) {
            String filePath = config.getFilePath();
            File file = new File(filePath);
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        return "";
    }
}
