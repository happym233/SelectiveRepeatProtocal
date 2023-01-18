package httpfs.HTTP;

import httpfs.Utils.FileHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    private FileHandler fileHandler;

    public RequestHandler(FileHandler handler) {
        this.fileHandler = handler;
    }

    private String generateMessage(int errorCode, String message) {
        if (errorCode == 404) {
            return message + " is not found.";
        } else if (errorCode == 405) {
            return message + " is not allowed.";
        } else if (errorCode == 500) {
            return "Internal error in server: \n" + message;
        } else if (errorCode == 400) {
            return "Bad request: \n" + message;
        }
        return "";
    }

    public String throwBadRequest() {
        Response response = new Response(400, null, generateMessage(400, "Bad Request"));
        return response.generate();
    }

    public String handle(String requestStr) {
       Request request = new Request();
       boolean valid = request.validate(requestStr);
       if (!valid) {
           Response response = new Response(400, null, generateMessage(400, requestStr));
           return response.generate();
       }
       String method = request.getHttpMethod();
       if (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("POST")) {
           Response response = new Response(405, null, generateMessage(405, method));
           return response.generate();
       }
       if (method.equalsIgnoreCase("get")) {
           try {
               String fileContent = fileHandler.read(request.getUrl());
               Map<String, String> header = new HashMap<>();
               String fileType = fileHandler.getMimeType(request.getUrl());
               if (fileType != null) {
                   header.put("Content-Type", fileType);
                   if (!fileType.equals("text/plain")) {
                       header.put("Content-Disposition", "attachment; filename=\"" + fileHandler.getFileName(request.getUrl()) + "\"");
                   }
               }
               Response response = new Response(200, header, fileContent);
               return response.generate();
           } catch (FileNotFoundException e) {
               Response response = new Response(404, null, generateMessage(404, request.getUrl()));
               return response.generate();
           } catch (IOException e) {
                Response response = new Response(500, null, generateMessage(500, "File is currently using by another client. Please try later."));
                return response.generate();
           }
       } else if (method.equalsIgnoreCase("post")) {
           try {
               fileHandler.write(request.getUrl(), request.getEntity());
               Response response = new Response(200, null, "Write successfully.");
               return response.generate();
           } catch (FileNotFoundException e) {
               Response response = new Response(404, null, generateMessage(404, request.getUrl()));
               return response.generate();
           } catch (IOException e) {
               Response response = new Response(500, null, generateMessage(500, e.toString()));
               return response.generate();
           } catch (OverlappingFileLockException e) {
            Response response = new Response(500, null, generateMessage(500, "File is currently using by another client. Please try later."));
            return response.generate();
        }
       }
       return "";
    }

}
