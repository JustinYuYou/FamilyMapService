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

                        if(urlComponent.length == 2) {
                            String eventID = urlComponent[(url.split("/").length - 1)];
                            SingleEventResponse response = eventService.readSingleEvent(eventID);
                            respData = new Gson().toJson(response);
                        } else {
                            AllEventResponse response = eventService.readAllEvent();

                            respData = new Gson().toJson(response);
                        }



                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);


                        // Now that the status code and headers have been sent to the client,
                        // next we send the JSON data in the HTTP response body.

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();

                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);

                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete
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
