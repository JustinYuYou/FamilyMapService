package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.RegisterRequest;
import response.RegisterResponse;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RegisterHandler extends ParentHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RegisterResponse registerResponse = null;
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                RegisterRequest r = new Gson().fromJson(reqData, RegisterRequest.class);

                RegisterService registerService = new RegisterService();
                registerResponse = registerService.register(r);

                if(registerResponse.isSuccess()) {
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
            String respData = new Gson().toJson(registerResponse);

            OutputStream respBody = exchange.getResponseBody();

            writeString(respData, respBody);

            respBody.close();
        }

    }
}
