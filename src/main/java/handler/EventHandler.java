package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import response.AllEventResponse;
import response.AllPersonResponse;
import response.SingleEventResponse;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler extends ParentHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");

                    if (authToken != null) {

                        String url = exchange.getRequestURI().toString();

                        String urlComponent[] = url.split("/");

                        EventService eventService = new EventService();
                        String respData;

                        if (urlComponent.length == 3) {
                            String eventID = urlComponent[(url.split("/").length - 1)];
                            SingleEventResponse response = eventService.readSingleEvent(eventID, authToken);
                            respData = new Gson().toJson(response);

                            if (response.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                            }
                        } else {
                            AllEventResponse response = eventService.readAllEvent(authToken);
                            respData = new Gson().toJson(response);


                            if (response.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                            }
                        }
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(respData, respBody);
                        respBody.close();
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }

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
