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
 * Поиск ССЫЛОК НА СТРАНИЦЫ видео
 */
@Component
public class HrefsWebpagesSearch {

    private String url;               // базовый url сайта, прописан в service.Properties
    private String serchMsgFormated;  // Отформатированный поисковый запрос, готовый к работе
    private String urlSearch;         // Полный url поискового запроса
    private ArrayList<String> hrefVideo;

    @Autowired
    private SearchingMessage searchingMessage;

    // ОСНОВНОЙ МЕТОД по поиску ссылокн страницы роликов
    public ArrayList<String> getHrefsOfVideos(String serchMsg) throws IOException {

        serchMsgFormated = searchingMessage.splitMessage(serchMsg);
        url = Properties.URL;
        urlSearch = url+ "/search/" +serchMsgFormated;

        // Парсим веб-страницу, полученную после поиска по поисковому выражению пользователя и получаем массив элементов ЗАГОЛОВКОВ
        Elements hrefsElements = parseDocument(urlSearch);

        // Получаем распарсенные и обработанные ССЫЛКИ к видео роликам и помещаем их в массив
        ArrayList<String> hrefsOfVideos = getHrefVideo(hrefsElements);

        return hrefsOfVideos;
    }



    // Метод парсинга веб-страницы с помощью библиотеки JSOUP
    private Elements parseDocument(String urlSearch) throws IOException {
        // Получаем JSOUP обьект страницы по адресу нашего запроса
        Document doc = Jsoup.connect(urlSearch).get();
        Elements videoElements = doc.getElementsByAttributeValue("class", "thumb");
        return videoElements;
    }



    // Возвращает массив ссылок на видео ролики
    public ArrayList<String> getHrefVideo(Elements hrefsElements) {

        hrefVideo = new ArrayList<String>(); // Массив для хранения ссылок на видео роликам

        for (Element video:hrefsElements) {
            String href = video.child(0).attr("href"); // достаемм содержимое поля href
            hrefVideo.add(url+href);
            System.out.println(url+href);
        }
        return hrefVideo;
    }


}