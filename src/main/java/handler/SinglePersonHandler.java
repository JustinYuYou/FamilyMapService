package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import response.SinglePersonResponse;
import service.SinglePersonService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SinglePersonHandler implements HttpHandler {
    private String personID;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // However, it does demonstrate the following:
        // 1. How to get the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to return the desired status code (200, 404, etc.)
        //		in an HTTP response
        // 4. How to write JSON data to the HTTP response body
        // 5. How to check an incoming HTTP request for an auth token

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow GET requests for this operation.
            // This operation requires a GET request, because the
            // client is "getting" information from the server, and
            // the operation is "read only" (i.e., does not modify the
            // state of the server).
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {

                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");

                    // Verify that the auth token is the one we're looking for
                    // (this is not realistic, because clients will use different
                    // auth tokens over time, not the same one all the time). A
                    // realistic example would do a database lookup to confirm that
                    // the auto token is valid and would retrieve the user data
                    // associated with the auth token.
                    if (authToken.equals("")) {

                        exchange.getRequestURI().toString();

                        SinglePersonService singlePersonService = new SinglePersonService();
                        SinglePersonResponse response = singlePersonService.readSinglePerson(personID);


                        // This is the JSON data we will return in the HTTP response body
                        // (this is unrealistic because it always returns the same answer).
                        // A realistic example would retrieve game data from the database
                        // and convert that to Json.
                        String respData = "";

                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);


                        // Now that the status code and headers have been sent to the client,
                        // next we send the JSON data in the HTTP response body.

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();

                        //do json

                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);

                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete
                        respBody.close();

                    } else {
                        // The auth token was invalid somehow, so we return a "not authorized"
                        // status code to the client.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
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
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    public void getSinglePerson(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            // Read and process response body from InputStream ...
        } else {
            // SERVER RETURNED AN HTTP ERROR

        }
    }



}
