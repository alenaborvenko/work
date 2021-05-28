package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.DAOImpl;
import model.InterfaceDAO;
import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Класс для обработки запросов
 */
public class WebHandler implements HttpHandler {
    private User user;
    private InterfaceDAO model = new DAOImpl();
    Gson gson = new Gson();
    //private int d = 0;
    String respJson;

    /**
     * Метод вызывающийся на событие
     * @param exchange - экземпляр класа HttpExcange, инкапсулирующий запрос клиента и ответ сервера
     * @throws IOException - ошибка открытия файлов
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        if (exchange.getRequestMethod().equals("GET")) {
            byte[] html = UtilsForResponse.getResponseForGET(uri);
            sendResponse(exchange, "text/html; charset=UTF8", html);
        } else {
                respJson = UtilsForResponse.getResponseForPost(uri, model, exchange.getRequestBody());
                sendResponse(exchange, "application/json", respJson.getBytes());
        }
    }
    private Passport getPassport(HttpExchange exchange) throws IOException, SQLException {
        InputStream requestBody = exchange.getRequestBody();
        Passport passport = gson.fromJson(getJsonString(requestBody), Passport.class);
        user = model.findByPassport(passport.getPassport());
        return passport;
    }

    /**
     * метод записывающий ответ клиенту
     * @param exchange экземпляр класа HttpExcange, инкапсулирующий запрос клиента и ответ сервера
     * @param contentType - тип ответа сервера
     * @param response - сам ответ
     * @throws IOException
     */
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

    /**
     * метод преобразующий запрос клиента в виде JSON в строку
     * @param in - сам запрос
     * @return - строка параметров запроса
     * @throws IOException - ошибки входного потока
     */
    private String getJsonString(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = in.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        return stringBuilder.toString();
    }
}
