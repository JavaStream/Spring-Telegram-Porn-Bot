package com.javastream.service;

import org.springframework.stereotype.Component;

/**
 * Данный класс позволяет преобразовать запрос клиента к формату нужному для поиска на сайте (вместо пробелов расставляет знак плюса)
 */

@Component
public class SearchingMessage {

    // Метод разбивающий предложение на отдельные слова и вставляющий плюс между ними
    public String splitMessage(String searcheMessage) {

        String splitMessageComplete = searcheMessage.replaceAll(" ", "+");

        return splitMessageComplete;
    }

}