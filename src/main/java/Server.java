import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The server for the FMS.
 */
public class Server {
    public static void main(String[] args){
        Server server = new Server();
        int port = 8080;

        try {
            server.startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //HttpServer Creation and Startup
    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer httpServer) {
        httpServer.createContext("/", new FileHandler());
        httpServer.createContext("/user/register", new RegisterHandler());
        httpServer.createContext("/user/login", new LoginHandler());
        httpServer.createContext("/clear", new ClearHandler());
        httpServer.createContext("/fill", new FillHandler());
        httpServer.createContext("/load", new LoadHandler());
        httpServer.createContext("/person", new PersonHandler());
        httpServer.createContext("/event", new EventHandler());
    }

}
