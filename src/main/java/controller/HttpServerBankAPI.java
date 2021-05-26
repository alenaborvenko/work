package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class HttpServerBankAPI {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 10);
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

    private static class WebHandler implements HttpHandler {
        private User user;
        private InterfaceDAO model = new DAOImpl();
        Passport passport;
        Gson gson = new Gson();
        private int d = 0;
        String respJson;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            if (exchange.getRequestMethod().equals("GET")) {
                if (uri.toString().equals("/")) {
                    byte[] html = getFromResourse("./src/main/java/view/pagesForUser/index.html");
                    sendResponse(exchange, "text/html; charset=UTF8", html);
                } else if (uri.toString().equals("/pagesForResult/addNewCard")) {
                    byte[] html = getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/addNewCard.html");
                    sendResponse(exchange, "text/html; charset=UTF", html);
                } else if (uri.toString().equals("/pagesForResult/checkBalance")) {
                    byte[] html = getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/checkBalance.html");
                    sendResponse(exchange, "text/html; charset=UTF", html);
                } else if (uri.toString().equals("/pagesForResult/findAllCards")) {
                    List<Card> card = null;
                    String respJson;
                    try {
                        card = model.findAllCard();
                        respJson = gson.toJson(card);
                    } catch (SQLException exception) {
                        respJson = gson.toJson("Error! Not found account");
                    }

                    sendResponse(exchange, "application/json; charset=UTF8", respJson.getBytes());
                } else if (uri.toString().equals("/pagesForResult/topUpBalance")) {
                    byte[] html = getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/topUpBalance.html");
                    sendResponse(exchange, "text/html; charset=UTF", html);
                }
            } else {
                if (uri.toString().equals("/welcome")) {
                    if (passport == null) {
                        passport = getPassport(exchange);
                    }
                    byte[] html;
                    if (user == null) {
                        html = getFromResourse("./src/main/java/view/exception/userNotFound.html");
                    } else {
                        html = getFromResourse("./src/main/java/view/pagesForUser/welcome.html");
                    }
                    sendResponse(exchange, "text/html; charset=UTF8", html);
                }  else if (uri.toString().equals("/pagesForResult/checkBalance")) {
                    if (d == 0) {
                        Account account = gson.fromJson(getJsonString(exchange.getRequestBody()), Account.class);
                        try {
                            BigDecimal balance = model.checkBalance(account.getNumber());
                            respJson = gson.toJson(balance);
                        } catch (SQLException exception) {
                            respJson = gson.toJson("Error! not found account");
                        }
                        d++;
                    } else {
                        d = 0;
                        sendResponse(exchange, "application/json; charset=UTF8", respJson.getBytes());
                    }
                } else if (uri.toString().equals("/pagesForResult/topUpBalance")) {
                    if (d == 0) {
                        AccountBalance account = gson.fromJson(getJsonString(exchange.getRequestBody()), AccountBalance.class);
                        try {
                            boolean toUp = model.topUpBalance(account.getNumber(), account.getBalance());
                            respJson = gson.toJson(toUp);
                        } catch (SQLException exception) {
                            respJson = gson.toJson("Error! not found account");
                        }
                        d++;
                    } else {
                        d = 0;
                        sendResponse(exchange, "application/json; charset=UTF8", respJson.getBytes());
                    }
                } else if (uri.toString().equals("/pagesForResult/addNewCard")) {
                    if (d == 0) {
                        Account account = gson.fromJson(getJsonString(exchange.getRequestBody()), Account.class);
                        try {
                            boolean toUp = model.addNewCard(account.getNumber());
                            respJson = gson.toJson(toUp);
                        } catch (SQLException exception) {
                            respJson = gson.toJson("Error! not found account");
                        }
                        d++;

                    } else {
                        d = 0;
                        sendResponse(exchange, "application/json; charset=UTF8", respJson.getBytes());
                    }
                }
            }
        }
        private Passport getPassport(HttpExchange exchange) throws IOException {
            InputStream requestBody = exchange.getRequestBody();
            Passport passport = gson.fromJson(getJsonString(requestBody), Passport.class);
            user = model.findByPassport(passport.getPassport());
            return passport;
        }

        private void sendResponse(HttpExchange exchange,String contentType, byte[] response) throws IOException {
            exchange.getResponseHeaders().set("Content-type", contentType);
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.flush();
            os.close();
        }

        private byte[] getFromResourse(String string) throws IOException {
            return Files.readAllBytes(Paths.get(string));
        }

        private String getJsonString(InputStream in) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            int currentLine;
            while ((currentLine = in.read()) != -1) {
                stringBuilder.append((char) currentLine);
            }
            return stringBuilder.toString();
        }
    }
}
