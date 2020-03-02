package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if(exchange.getRequestMethod().toUpperCase().equals("GET")) {
            String urlPath = exchange.getRequestURI().toString();

            System.out.println(urlPath);
            if(urlPath == null || urlPath.equals("/") || urlPath.equals("")) {
                urlPath = "/index.html";
            }

            String workingDirectory = "web" + urlPath;
            File file = new File(workingDirectory);
            System.out.println(workingDirectory);

            if(!file.exists()) {
                workingDirectory = "web/HTML/404.html";
                file = new File(  "web/HTML/404.html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }

            Path filePath = FileSystems.getDefault().getPath(workingDirectory);
            OutputStream respBody = exchange.getResponseBody();
            Files.copy(filePath, respBody);
            respBody.close();

        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }
}
