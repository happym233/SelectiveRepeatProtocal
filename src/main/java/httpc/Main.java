package httpc;

import httpc.Utils.Config;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        config.parse(args);
        Client client = new Client(config);
        client.sendRequest();

//        HTTP http = new HTTP(true);
//        Map<String, String> header = new HashMap<>();
//        header.put("Content-Type", "multipart/form-data; boundary=xxBOUNDARYxx");
//        header.put("content-disposition", "form-data;name=\"filefield\";filename=\"some.txt\"");
//        System.out.println(http.request("POST", "http://httpbin.org/post", header, "--xxBOUNDARYxx\n" +
//                "Content-Disposition: form-data; name=\"profileImage\"; filename=\"a.txt\"\n" +
//                "Content-Type: text/plain\n\n" + "11111111111111111111111111\n" +
//                "--xxBOUNDARYxx--"));
    }
}
