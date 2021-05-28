package controller;

import com.google.gson.Gson;
import model.Card;
import model.InterfaceDAO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

/**
 *Класс утилитный для обработки запросов и возвращения результата
 */
public class UtilsForResponse {
    private static Gson gson = new Gson();
    private static String respJson;

    /**
     * Метод получения ответов для Get запросов
     * @param uri - с какого адреса пришел запрос
     * @return - строка html страницы
     * @throws IOException - ошибки открытия файлов
     */
    public static byte[] getResponseForGET(URI uri) throws IOException {
        byte[] rsl = new byte[0];
        if (uri.toString().equals("/")) {
        rsl =  getFromResourse("./src/main/java/view/pagesForUser/welcome.html");
    } else if (uri.toString().equals("/pagesForResult/addNewCard")) {
        rsl =  getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/addNewCard.html");
    } else if (uri.toString().equals("/pagesForResult/checkBalance")) {
        rsl =  getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/checkBalance.html");
    } else if (uri.toString().equals("/pagesForResult/findAllCards")) {
            rsl =  getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/findAllCards.html");
    } else if (uri.toString().equals("/pagesForResult/topUpBalance")) {
        return getFromResourse("./src/main/java/view/pagesForUser/pagesForResult/topUpBalance.html");
    }
        return rsl;
    }

    /**
     * Метод для обработки Post запросов
     * @param uri - адрес с которого пришел запрос
     * @param model - модель базы данных (post запросы оперируют с базой данной)
     * @param inputStream - параметры запроса
     * @return - строку в формате JSON
     * @throws IOException - ошибки входного потока
     */
    public static String getResponseForPost(URI uri, InterfaceDAO model, InputStream inputStream) throws IOException {
        if (uri.toString().equals("/pagesForResult/checkBalance/result")) {
            //if (d == 0) {
                PassportAccount account = gson.fromJson(getJsonString(inputStream), PassportAccount.class);
                try {
                    BigDecimal balance = model.checkBalance(account.getAccount(), account.getPassport());
                    respJson = gson.toJson(balance);
                } catch (Exception exception) {
                    respJson = gson.toJson("Error! not found account");
                }
             //   d++;
            //} else {
            //    d = 0;
           // }
        } else if (uri.toString().equals("/pagesForResult/topUpBalance/result")) {
           // if (d == 0) {
                AccountBalance account = gson.fromJson(getJsonString(inputStream), AccountBalance.class);
                try {
                    boolean toUp = model.topUpBalance(account.getNumber(), account.getBalance(), account.getPassport());
                    respJson = gson.toJson(toUp);
                } catch (Exception exception) {
                    respJson = gson.toJson("Error! not found account");
                }
            //    d++;
           // } else {
            //    d = 0;
            //}
        } else if (uri.toString().equals("/pagesForResult/addNewCard/result")) {
           // if (d == 0) {
                PassportAccount account = gson.fromJson(getJsonString(inputStream), PassportAccount.class);
                try {
                    boolean toUp = model.addNewCard(account.getAccount(), account.getPassport());
                    respJson = gson.toJson(toUp);
                } catch (Exception exception) {
                    respJson = gson.toJson("Error! not found account");
                }
             //   d++;

         //   } else {
         //       d = 0;
         //   }
        } else if (uri.toString().equals("/pagesForResult/findAllCards/result")) {
         //   if (d == 0) {
                Passport passport = gson.fromJson(getJsonString(inputStream), Passport.class);
                try {
                    List<Card> card = model.findAllCard(passport.getPassport());
                    respJson = gson.toJson(card);
                } catch (SQLException exception) {
                    respJson = gson.toJson("Error! Not found account");
                }
           //     d++;
          //  } else {
          //      d = 0;
           // }
        }
         return respJson;
    }

public static byte[] getFromResourse(String string) throws IOException {
        return Files.readAllBytes(Paths.get(string));
        }

private static String getJsonString(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int currentLine;
        while ((currentLine = in.read()) != -1) {
        stringBuilder.append((char) currentLine);
        }
        return stringBuilder.toString();
        }
}
