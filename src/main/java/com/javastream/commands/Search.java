package com.javastream.commands;

import com.javastream.search.HeadersSearch;
import com.javastream.search.HrefsWebpagesSearch;
import com.javastream.search.ImagesSearch;
import com.javastream.search.MP4Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Search {

    private Message              message;
    private ArrayList<String>    headersSearchList;         // Заголовки к видео
    private ArrayList<String>    hrefsWebpagesSearchList;   // Ссылки на на страницы с видео
    private ArrayList<String>    imagesSearchList;          // Ссылки на картинки
    private ArrayList<String>    mp4SearchList;             // Ссылки на MP4 (в планах!)

    @Autowired
    private HeadersSearch headersSearch;

    @Autowired
    private HrefsWebpagesSearch hrefsWebpagesSearch;

    @Autowired
    private ImagesSearch imagesSearch;

    @Autowired
    private MP4Search mp4Search;

    public Search() {
        headersSearchList       = new ArrayList<String>();
        hrefsWebpagesSearchList = new ArrayList<String>();
        imagesSearchList        = new ArrayList<String>();
        mp4SearchList           = new ArrayList<String>();
    }


    public void init(Message message) {
        this.message = message;

        // Очищаем массивы с заголовками, фото и ссылками перед новым поисковым запросом
        if (!headersSearchList.isEmpty())             { headersSearchList.clear(); }
        if (!hrefsWebpagesSearchList.isEmpty())       { hrefsWebpagesSearchList.clear(); }
        if (!imagesSearchList.isEmpty())              { imagesSearchList.clear(); }
        if (!mp4SearchList.isEmpty())                 { mp4SearchList.clear(); }

        // Поисковый запрос без /http. По этим поисковым словам будем выдергивать контент из сайта с видео
        String messageText = message.getText();
        String searchingMessage = messageText.replaceAll("/find", "");

        // Собираем массивы данных, которые далее будем использовать для выдачи юзеру
        try {
            headersSearchList = headersSearch.getHeadersOfVideos(searchingMessage);
            hrefsWebpagesSearchList = hrefsWebpagesSearch.getHrefsOfVideos(searchingMessage);
            imagesSearchList = imagesSearch.getImages(searchingMessage);
            mp4SearchList = mp4Search.getHrefsOfMP4(searchingMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Getters and Setters
    public ArrayList<String> getHeadersSearchList() {
        return headersSearchList;
    }

    public void setHeadersSearchList(ArrayList<String> headersSearchList) {
        this.headersSearchList = headersSearchList;
    }

    public ArrayList<String> getHrefsWebpagesSearchList() {
        return hrefsWebpagesSearchList;
    }

    public void setHrefsWebpagesSearchList(ArrayList<String> hrefsWebpagesSearchList) {
        this.hrefsWebpagesSearchList = hrefsWebpagesSearchList;
    }

    public ArrayList<String> getImagesSearchList() {
        return imagesSearchList;
    }

    public void setImagesSearchList(ArrayList<String> imagesSearchList) {
        this.imagesSearchList = imagesSearchList;
    }

    public ArrayList<String> getMp4SearchList() {
        return mp4SearchList;
    }

    public void setMp4SearchList(ArrayList<String> mp4SearchList) {
        this.mp4SearchList = mp4SearchList;
    }

    public Message getMessage() {
        return message;
    }
}
