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
        System.out.println("We reached here\n");
        FillResponse fillResponse = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                String url = exchange.getRequestURI().toString();
                String urlComponent[] = url.split("/");

                String username = urlComponent[2];
                int generations = Integer.parseInt(urlComponent[3]);
//                if(urlComponent.length == 2) {
//                    username = urlComponent[1];
//                }

                FillRequest r = new FillRequest(username, generations);
                FillService fillService = new FillService();
                fillResponse = fillService.fill(r);

                if (fillResponse.isSuccess()) {
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
            String respData = new Gson().toJson(fillResponse);

            OutputStream respBody = exchange.getResponseBody();

            writeString(respData, respBody);

            respBody.close();
        }
    }
}
