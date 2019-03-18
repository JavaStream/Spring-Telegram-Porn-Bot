package com.javastream.commands;

import com.javastream.search.HeadersSearch;
import com.javastream.search.HrefsWebpagesSearch;
import com.javastream.search.ImagesSearch;
import com.javastream.search.MP4Search;
import com.javastream.service.SendingPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Find {

    @Autowired
    private HeadersSearch headersSearch;

    @Autowired
    private HrefsWebpagesSearch hrefsWebpagesSearch;

    @Autowired
    private ImagesSearch imagesSearch;

    @Autowired
    private MP4Search mp4Search;

    @Autowired
    private SendingPhoto sendingPhoto;

    private ArrayList<String> headersSearchList;
    private ArrayList<String> hrefsWebpagesSearchList;
    private ArrayList<String> imagesSearchList;
    private ArrayList<String> mp4SearchList;
    private ArrayList<SendPhoto> arrayListSendPhoto;

    public Find() {
        headersSearchList       = new ArrayList<String>();
        hrefsWebpagesSearchList = new ArrayList<String>();
        imagesSearchList        = new ArrayList<String>();
        mp4SearchList           = new ArrayList<String>();
        arrayListSendPhoto      = new ArrayList<SendPhoto>();
    }

    public void setArraysData(Message message) {

        // Очищаем массивы с заголовками, фото и ссылками перед новым поисковым запросом
        if (!headersSearchList.isEmpty())             { headersSearchList.clear(); }
        if (!hrefsWebpagesSearchList.isEmpty())       { hrefsWebpagesSearchList.clear(); }
        if (!imagesSearchList.isEmpty())              { imagesSearchList.clear(); }
        if (!mp4SearchList.isEmpty())                 { mp4SearchList.clear(); }
        if (!arrayListSendPhoto.isEmpty())            { arrayListSendPhoto.clear(); }


        // Поисковый запрос без /http. По этим поисковым словам будем выдергивать видео из поиска сайта с видео
        String messageText = message.getText();
        String searchingMessage = messageText.replaceAll("/find", "");

        try {
            headersSearchList = headersSearch.getHeadersOfVideos(searchingMessage);
            hrefsWebpagesSearchList = hrefsWebpagesSearch.getHrefsOfVideos(searchingMessage);
            imagesSearchList = imagesSearch.getImages(searchingMessage);
            mp4SearchList = mp4Search.getHrefsOfMP4(searchingMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Возвращает массив объектов SendingPhoto, которые в основном классе бота могут быть выполнены командой execute
    public ArrayList<SendPhoto> findCommand(Message message) throws TelegramApiException {

        // Метод возвращает массивы данных по нашему запросу (заголовки, ссылки на видео и картинки)
        setArraysData(message);

        for (int i=0; i < 5; i++) {

            String caption = headersSearchList.get(i);
            String href = hrefsWebpagesSearchList.get(i);
            String urlImg = imagesSearchList.get(i);
            String urlMP4 = mp4SearchList.get(i);

            SendPhoto sendPhoto = sendingPhoto.sendPhoto(message, caption, href, urlImg); // Отправляем фото с подписью и ссылкой
            this.arrayListSendPhoto.add(sendPhoto);
        }

        return arrayListSendPhoto;
    }



    public ArrayList<String> getHeadersSearchList() {
        return headersSearchList;
    }

    public ArrayList<String> getHrefsWebpagesSearchList() {
        return hrefsWebpagesSearchList;
    }

    public ArrayList<String> getImagesSearchList() {
        return imagesSearchList;
    }

    public ArrayList<String> getMp4SearchList() {
        return mp4SearchList;
    }

    public ArrayList<SendPhoto> getArrayListSendPhoto() {
        return arrayListSendPhoto;
    }
}
