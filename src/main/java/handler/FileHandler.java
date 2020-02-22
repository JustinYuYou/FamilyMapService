package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if(exchange.getRequestMethod().toUpperCase().equals("GET")) {
            Headers reqHeaders = exchange.getRequestHeaders();

            String urlPath = exchange.getRequestURI().toString();
            if(urlPath == null || urlPath == "/") {
                urlPath = "/index.html";
            }

            String filePath = "" + urlPath;

            File webFile = new File(filePath);
            if(!webFile.exists()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            }

        } else {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }
}
