package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import request.LoadRequest;
import response.FillResponse;
import response.LoadResponse;
import service.FillService;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler extends ParentHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");
//                    InputStream reqBody = exchange.getRequestBody();
//                    String reqData = readString(reqBody);
                    String url = exchange.getRequestURI().toString();
                    String urlComponent[] = url.split("/");
                    String username = urlComponent[1];
                    int generations = Integer.parseInt(urlComponent[2]);
                    FillRequest r = new FillRequest(username, generations);

                    FillService fillService = new FillService();
                    FillResponse fillResponse = fillService.fill(r);

                    String respData = new Gson().toJson(fillResponse);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    OutputStream respBody = exchange.getResponseBody();

                    writeString(respData, respBody);

                    respBody.close();
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }
}
