package httpfs.Utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static String defaultRoot = System.getProperty("user.dir");
    private Path rootPath;
    private File rootDir;

    public FileHandler() throws IOException {
        this("");
    }

    public FileHandler(String path) throws IOException {
        rootPath = Paths.get(defaultRoot, path);
        rootDir = new File(rootPath.toString());
        if (!rootDir.isDirectory()) {
            throw new IOException(path + " is not a directory.");
        }
    }

    public String read(String path) throws IOException {
        File targetFile = new File(rootDir, path);
        if (!targetFile.exists()) {
            throw new FileNotFoundException(path + " not found.");
        } else if (targetFile.isDirectory()) {
            List<String> files = new ArrayList<>();
            for (File file: targetFile.listFiles()) {
                files.add(file.getName());
            }
            return String.join(", ", files);
        } else {
            FileReader fileReader = new FileReader(targetFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return sb.toString();
        }
    }

    public String getMimeType(String filePath) throws IOException {
        Path path = new File(rootDir, filePath).toPath();
        return Files.probeContentType(path);
    }

    public String getFileName(String filePath) {
        Path path = Path.of(filePath);
        return path.getFileName().toString();
    }

    public void write(String path, String contents) throws IOException {
        File targetFile = new File(rootDir, path);
        Files.createDirectories(targetFile.getParentFile().toPath());
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw")){
            FileChannel channel = randomAccessFile.getChannel();
            FileLock lock = channel.lock();
            char[] contentChars = contents.toCharArray();
            for (char c: contentChars) {
                randomAccessFile.write(c);
            }
            Thread.sleep(10000);
            lock.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
