package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import request.LoginRequest;
import response.LoadResponse;
import response.LoginResponse;
import service.LoadService;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler extends ParentHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LoadResponse loadResponse = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoadRequest r = new Gson().fromJson(reqData, LoadRequest.class);

                LoadService loadService = new LoadService();
                loadResponse = loadService.load(r);

                if(loadResponse.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        } finally {
            String respData = new Gson().toJson(loadResponse);
            OutputStream respBody = exchange.getResponseBody();
            writeString(respData, respBody);
            respBody.close();
        }
    }

}
