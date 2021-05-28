package controller;

import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class HttpServerBankAPITest {
     static HttpServer server;

    @BeforeClass
    public static void setUp() throws IOException, SQLException {
        server = HttpServerBankAPI.getInstance();
    }

    @Test
    public void whengetResponseForPostAddNewCard() throws IOException {
        URL url1 = new URL("http://localhost:8001/pagesForResult/addNewCard/result");
        URLConnection connection = url1.openConnection();
        connection.setDoOutput(true);
        String request = "{\"passport\":\"5010 800820\",\"account\":\"10000000000000000000\"}";
        OutputStream query1 = connection.getOutputStream();
        query1.write(request.getBytes());
        query1.flush();
        query1.close();
        InputStream inputStream1 = connection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream1.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream1.close();
        assertEquals("true", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostAddNewCardUserNotFound() throws IOException {
        URL url1 = new URL("http://localhost:8001/pagesForResult/addNewCard/result");
        URLConnection connection = url1.openConnection();
        connection.setDoOutput(true);
        String request = "{\"passport\":\"5010 800830\",\"account\":\"10000000000000000000\"}";
        OutputStream query1 = connection.getOutputStream();
        query1.write(request.getBytes());
        query1.flush();
        query1.close();
        InputStream inputStream1 = connection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream1.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream1.close();
        assertEquals("\"Error! not found account\"", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostCheckBalanceNotFound() throws IOException {
        URL url1 = new URL("http://localhost:8001/pagesForResult/checkBalance/result");
        URLConnection connection = url1.openConnection();
        connection.setDoOutput(true);
        String request = "{\"passport\":\"5010 800820\",\"account\":\"10000000000000000005\"}";
        OutputStream query1 = connection.getOutputStream();
        query1.write(request.getBytes());
        query1.flush();
        query1.close();
        InputStream inputStream1 = connection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream1.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream1.close();
        assertEquals("\"Error! not found account\"", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostFindAllCardsNotFound() throws IOException {
        URL url1 = new URL("http://localhost:8001/pagesForResult/findAllCards/result");
        URLConnection connection = url1.openConnection();
        connection.setDoOutput(true);
        String request = "{\"passport\":\"5010 800920\"}";
        OutputStream query1 = connection.getOutputStream();
        query1.write(request.getBytes());
        query1.flush();
        query1.close();
        InputStream inputStream1 = connection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream1.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream1.close();
        assertEquals("[]", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPosttopUpBalanceNotFound() throws IOException {
        URL url1 = new URL("http://localhost:8001/pagesForResult/topUpBalance/result");
        URLConnection connection = url1.openConnection();
        connection.setDoOutput(true);
        String request = "{\"passport\":\"5010 800820\",\"account\":\"10000000000000000005\",\"balance\":\"90\"}";
        OutputStream query1 = connection.getOutputStream();
        query1.write(request.getBytes());
        query1.flush();
        query1.close();
        InputStream inputStream1 = connection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream1.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream1.close();
        assertEquals("\"Error! not found account\"", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostCheckBalance() throws IOException {
        URL url2 = new URL("http://localhost:8001/pagesForResult/checkBalance/result");
        URLConnection connection2 = url2.openConnection();
        connection2.setDoOutput(true);
        String request = "{\"passport\":\"5010 800820\",\"account\":\"10000000000000000000\"}";
        OutputStream query2 = connection2.getOutputStream();
        query2.write(request.getBytes());
        query2.flush();
        query2.close();
        InputStream inputStream2 = connection2.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream2.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream2.close();
        assertEquals("100.00", stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostFindAllCard() throws IOException {
        URL url3 = new URL("http://localhost:8001/pagesForResult/findAllCards/result");
        URLConnection connection3 = url3.openConnection();
        connection3.setDoOutput(true);
        String request = "{\"passport\":\"5010 800820\",\"account\":\"10000000000000000000\"}";
        OutputStream query3 = connection3.getOutputStream();
        query3.write(request.getBytes());
        query3.flush();
        query3.close();
        InputStream inputStream3 = connection3.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream3.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream3.close();
        String expected = "[{\"id\":1000000000000000,\"account\":{\"id\":1,\"number\":\"10000000000000000000\",\"balance\":100.00}}]";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseGETwelcome() throws IOException {
        URL url3 = new URL("http://localhost:8001/");
        URLConnection connection3 = url3.openConnection();
        StringBuilder stringBuilder;
        try (BufferedReader inputStream3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()))) {
            stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = inputStream3.readLine()) != null) {
                stringBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        String expected = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                "<head>\n" +
                "    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF8\">\n" +
                "    <title>Welcome</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body align = 'center'>\n" +
                "    <p>Добро пожаловать</p><br>\n" +
                "    <a href=\"/pagesForResult/addNewCard\">Добавить новую карту</a><br>\n" +
                "    <a href=\"/pagesForResult/checkBalance\">Проверить баланс счета</a><br>\n" +
                "    <a href=\"/pagesForResult/findAllCards\">Показать все карты</a><br>\n" +
                "    <a href=\"/pagesForResult/topUpBalance\">Пополнить счет</a><br>\n" +
                "</body>\n" +
                "</html>" + System.lineSeparator();
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseGETAddNewCard() throws IOException {
        URL url3 = new URL("http://localhost:8001/pagesForResult/addNewCard");
        URLConnection connection3 = url3.openConnection();
        StringBuilder stringBuilder;
        try (BufferedReader inputStream3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()))) {
            stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = inputStream3.readLine()) != null) {
                stringBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        String expected = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                "<head>\n" +
                "    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF8\">\n" +
                "    <title>Вход</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<form name=\"auto\" method=\"post\" id = 'auto' align = 'center' size = \"100\" onsubmit=\"sendJSON()\">\n" +
                "    <p><b>Введите номер и серию паспорта</b><br>\n" +
                "        <input name =\"passport\" type=\"text\" size=\"40\" id = \"passport\">\n" +
                "    <p><b>Введите номер счета</b><br>\n" +
                "        <input name =\"account\" type=\"text\" size=\"40\" id = \"account\">\n" +
                "    <p><input type=\"submit\" value=\"Ввод\" onsubmit=\"sendJSON();\">\n" +
                "</form>\n" +
                "<script type=\"text/javascript\">\n" +
                "    function sendJSON() {\n" +
                "        let form = document.forms[\"auto\"];\n" +
                "        let fd = new FormData(form);\n" +
                "        let data = {};\n" +
                "        for (let [key, prop] of fd) {\n" +
                "            data[key] = prop;\n" +
                "        }\n" +
                "        data = JSON.stringify(data, null, 2);\n" +
                "        window.location.href = \"http://localhost:8001/pagesForResult/addNewCard/result\";\n" +
                "        let xmlhttp;\n" +
                "        xmlhttp = new XMLHttpRequest();\n" +
                "        xmlhttp.open(\"POST\", \"/pagesForResult/addNewCard/result\", true);\n" +
                "        xmlhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "        xmlhttp.send(data);\n" +
                "        return false;\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseGETCheckBalance() throws IOException {
        URL url3 = new URL("http://localhost:8001/pagesForResult/checkBalance");
        URLConnection connection3 = url3.openConnection();
        StringBuilder stringBuilder;
        try (BufferedReader inputStream3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()))) {
            stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = inputStream3.readLine()) != null) {
                stringBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        String expected = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                "<head>\n" +
                "    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF8\">\n" +
                "    <title>Вход</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<form name=\"auto\" method=\"post\" id = 'auto' align = 'center' onsubmit=\"sendJSON()\">\n" +
                "    <p><b>Введите номер и серию паспорта</b><br>\n" +
                "        <input name =\"passport\" type=\"text\" size=\"40\" id = \"passport\">\n" +
                "    <p><b>Введите номер счета</b><br>\n" +
                "        <input name =\"account\" type=\"text\" size=\"40\" id = \"account\">\n" +
                "    <p><input type=\"submit\" value=\"Ввод\" onclick=\"sendJSON()\">\n" +
                "</form>\n" +
                "<script type=\"text/javascript\">\n" +
                "    function sendJSON() {\n" +
                "        let form = document.forms[\"auto\"];\n" +
                "        let fd = new FormData(form);\n" +
                "        let data = {};\n" +
                "        for (let [key, prop] of fd) {\n" +
                "            data[key] = prop;\n" +
                "        }\n" +
                "        data = JSON.stringify(data, null, 2);\n" +
                "\n" +
                "        let xmlhttp;\n" +
                "        window.location.href = \"http://localhost:8001/pagesForResult/checkBalance/result\";\n" +
                "        xmlhttp = new XMLHttpRequest();\n" +
                "        xmlhttp.open(\"POST\", \"/pagesForResult/checkBalance/result\", true);\n" +
                "        xmlhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "        xmlhttp.send(data);\n" +
                "        return false;\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseGETFindAllCards() throws IOException {
        URL url3 = new URL("http://localhost:8001/pagesForResult/findAllCards");
        URLConnection connection3 = url3.openConnection();
        StringBuilder stringBuilder;
        try (BufferedReader inputStream3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()))) {
            stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = inputStream3.readLine()) != null) {
                stringBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        String expected = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                "<head>\n" +
                "    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF8\">\n" +
                "    <title>Вход</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<form name=\"auto\" method=\"post\" id = 'auto' align = 'center' size = \"100\" onsubmit=\"sendJSON()\">\n" +
                "    <p><b>Введите серию и номер паспорта</b><br>\n" +
                "        <input name =\"passport\" type=\"text\" size=\"40\" id = \"passport\">\n" +
                "    <p><input type=\"submit\" value=\"Ввод\" onclick=\"sendJSON()\">\n" +
                "</form>\n" +
                "<script type=\"text/javascript\">\n" +
                "    function sendJSON() {\n" +
                "        let form = document.forms[\"auto\"];\n" +
                "        let fd = new FormData(form);\n" +
                "        let data = {};\n" +
                "        for (let [key, prop] of fd) {\n" +
                "            data[key] = prop;\n" +
                "        }\n" +
                "        data = JSON.stringify(data, null, 2);\n" +
                "\n" +
                "        let xmlhttp;\n" +
                "        window.location.href = \"http://localhost:8001/pagesForResult/findAllCards/result\";\n" +
                "        xmlhttp = new XMLHttpRequest();\n" +
                "        xmlhttp.open(\"POST\", \"/pagesForResult/findAllCards/result\", true);\n" +
                "        xmlhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "        xmlhttp.send(data);\n" +
                "        return false;\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseGETtopUpBalance() throws IOException {
        URL url3 = new URL("http://localhost:8001/pagesForResult/topUpBalance");
        URLConnection connection3 = url3.openConnection();
        StringBuilder stringBuilder;
        try (BufferedReader inputStream3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()))) {
            stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = inputStream3.readLine()) != null) {
                stringBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        String expected = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                "<head>\n" +
                "    <META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF8\">\n" +
                "    <title>Вход</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<form name=\"toUpBalance\" method=\"post\" id = 'auto'align = 'center'>\n" +
                "    <p><b>Введите номер и серию счета</b><br>\n" +
                "        <input name =\"passport\" type=\"text\" size=\"40\" id = \"passport\">\n" +
                "    <p><b>Введите номер счета</b><br>\n" +
                "        <input name =\"account\" type=\"text\" size=\"40\" id = \"account\">\n" +
                "    <p><b>Введите баланс</b><br>\n" +
                "        <input name =\"balance\" type=\"text\" size=\"40\" id = \"balance\">\n" +
                "    <p><input type=\"submit\" value=\"Ввод\" onclick=\"sendJSON()\">\n" +
                "</form>\n" +
                "<script type=\"text/javascript\">\n" +
                "    function sendJSON() {\n" +
                "        let form = document.forms[\"toUpBalance\"];\n" +
                "        let fd = new FormData(form);\n" +
                "        let data = {};\n" +
                "        for (let [key, prop] of fd) {\n" +
                "            data[key] = prop;\n" +
                "        }\n" +
                "        data = JSON.stringify(data, null, 2);\n" +
                "\n" +
                "        window.location.href = \"http://localhost:8001/pagesForResult/topUpBalance/result\";\n" +
                "        let xmlhttp;\n" +
                "        xmlhttp = new XMLHttpRequest();\n" +
                "        xmlhttp.open(\"POST\", \"/pagesForResult/topUpBalance/result\", true);\n" +
                "        xmlhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "        xmlhttp.send(data);\n" +
                "        return false;\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void whengetResponseForPostToUpBalance() throws IOException {
        URL url4 = new URL("http://localhost:8001/pagesForResult/topUpBalance/result");
        URLConnection connection4 = url4.openConnection();
        connection4.setDoOutput(true);
        String request = "{\"passport\":\"5011 90760\",\"account\":\"10000000000000000001\",\"balance\":\"700\"}";
        OutputStream query4 = connection4.getOutputStream();
        query4.write(request.getBytes());
        query4.flush();
        query4.close();
        InputStream inputStream4 = connection4.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = inputStream4.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream4.close();
        String expected = "true";
        assertEquals(expected, stringBuilder.toString());
        URL url5 = new URL("http://localhost:8001/pagesForResult/checkBalance/result");
        URLConnection connection5 = url5.openConnection();
        connection5.setDoOutput(true);
        request = "{\"passport\":\"5011 90760\",\"account\":\"10000000000000000001\"}";
        OutputStream query5 = connection5.getOutputStream();
        query5.write(request.getBytes());
        query5.flush();
        query5.close();
        InputStream inputStream5 = connection5.getInputStream();
        stringBuilder = new StringBuilder();
        while ((currentLine = inputStream5.read()) != -1) {
            stringBuilder.append((char) currentLine);
        }
        inputStream5.close();
        assertEquals("900.00", stringBuilder.toString());
    }

    @AfterClass
    public static void after() {
        server.stop(1);
    }
}