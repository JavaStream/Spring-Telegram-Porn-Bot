package com.javastream.search;

import com.javastream.service.Properties;
import com.javastream.service.SearchingMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Поиск ЗАГОЛОВКОВ к видео (названия роликов)
 */

@Component
public class HeadersSearch {

    private String url;               // базовый url сайта, прописан в service.Properties
    private String serchMsgFormated;  // Отформатированный поисковый запрос, готовый к работе
    private String urlSearch;         // Полный url поискового запроса
    private ArrayList<String> headlineVideo;

    @Autowired
    private SearchingMessage searchingMessage;

    // ОСНОВНОЙ МЕТОД по поиску текстовых названий роликов
    public ArrayList<String> getHeadersOfVideos(String serchMsg) throws IOException {

        serchMsgFormated = searchingMessage.splitMessage(serchMsg);

        url = Properties.URL;
        urlSearch = url+ "/search/" +serchMsgFormated;

        // Парсим веб-страницу, полученную после поиска по поисковому выражению пользователя и получаем массив элементов ЗАГОЛОВКОВ
        Elements headerElements = parseDocument(urlSearch);

        // Обрабатываем ЗАГОЛОВКИ к видео роликам и помещаем их в массив
        ArrayList<String> headersVideo = getArraysOfHreffs(headerElements);

        return headersVideo;
    }


    // Метод парсинга веб-страницы с помощью библиотеки JSOUP
    public Elements parseDocument(String urlSearch) throws IOException {
        // Получаем JSOUP обьект страницы по адресу нашего запроса
        Document doc = Jsoup.connect(urlSearch).get();
        Elements headerElements = doc.getElementsByAttributeValue("class", "thumb-under");
        return headerElements;
    }


    // Возвращает массив заголовков к видео
    public ArrayList<String> getArraysOfHreffs(Elements headerElements) {

        headlineVideo = new ArrayList<String>(); // Массив для хранения заголовков к видео роликам

        // Заполняем массив текстами заголовков к видео
        for (Element headlineElement:headerElements) {
            String headline = headlineElement.child(0).text();
            headlineVideo.add(headline);
            System.out.println(headline);
        }
        return headlineVideo;
    }

}