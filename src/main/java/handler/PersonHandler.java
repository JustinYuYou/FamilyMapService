package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import response.AllPersonResponse;
import response.SinglePersonResponse;
import service.PersonService;

import java.io.*;
import java.net.HttpURLConnection;

public class PersonHandler extends ParentHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");

                    String url = exchange.getRequestURI().toString();

                    String urlComponent[] = url.split("/");
                    
                    //If it has personID info, it will be getting one person
                    PersonService personService = new PersonService();
                    String respData;
                    
                    if (urlComponent.length == 2) {
                        String personID = urlComponent[urlComponent.length - 1];
                        SinglePersonResponse response = personService.readSinglePerson(personID);

                        respData = new Gson().toJson(response);
                    } else {
                        AllPersonResponse response = personService.readAllPerson();
                        
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
                    // We did not get an auth token, so we return a "not authorized"
                    // status code to the client.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                // We expected a GET but got something else, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the , so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }


}
