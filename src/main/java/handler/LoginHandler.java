package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import request.LoginRequest;
import response.LoginResponse;
import service.LoginService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.Provider;

public class LoginHandler extends ParentHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LoginResponse loginResponse = null;
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);
                    LoginRequest r = new Gson().fromJson(reqData, LoginRequest.class);

                    LoginService loginService = new LoginService();
                    loginResponse = loginService.login(r);

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        } finally {
            String respData = new Gson().toJson(loginResponse);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStream respBody = exchange.getResponseBody();

            writeString(respData, respBody);

            respBody.close();
        }
    }
}