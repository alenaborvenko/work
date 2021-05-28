package controller;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Класс запускающий сервер
 */
public class HttpServerBankAPI {
    public static void main(String[] args) throws IOException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 10);
        server.createContext("/", new WebHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("server starts");
        Scanner scanner = new Scanner(System.in);
        String str;
        do {
            str = scanner.nextLine();
        } while (!"exit".equals(str));
        server.stop(0);
        scanner.close();
    }

    public static HttpServer getInstance() throws IOException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 10);
        server.createContext("/", new WebHandler());
        server.setExecutor(null);
        server.start();
        return server;
    }
}
