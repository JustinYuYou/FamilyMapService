package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import response.ClearResponse;
import service.ClearService;

import java.io.*;
import java.net.HttpURLConnection;

/**
 *
 */
public class ClearHandler extends ParentHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ClearResponse clearResponse = null;
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                ClearService clearService = new ClearService();
                clearResponse = clearService.clear();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            e.printStackTrace();
        } finally {
            String respData = new Gson().toJson(clearResponse);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStream respBody = exchange.getResponseBody();

            writeString(respData, respBody);

            respBody.close();
        }
    }
}
