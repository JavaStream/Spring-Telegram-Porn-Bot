package com.javastream.commands;

import com.javastream.search.HeadersSearch;
import com.javastream.search.HrefsWebpagesSearch;
import com.javastream.search.ImagesSearch;
import com.javastream.search.MP4Search;
import com.javastream.service.DownloaderMP4;
import com.javastream.service.SendingPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Serg on 13.03.2019.
 */
public class Find {

    ArrayList<String> arrayHeaders;   // Массив заголовков (caption)
    ArrayList<String> arrayHrefs;     // Массив ссылок (href)
    ArrayList<String> arrUrlImg;      // Массив ссылок на картинку (urlImg)
    ArrayList<String> arrMP4links;    // Массив ссылок на MP4 ролики

    public Find() {
        this.arrayHeaders = new ArrayList<String>();
        this.arrayHrefs = new ArrayList<String>();
        this.arrUrlImg = new ArrayList<String>();
        this.arrMP4links = new ArrayList<String>();
    }

    // Метод отвечает за наполнение массива заголовков, ссылок на видео, ссылок на картинки и mp4
    public void setArraysData(Message message) {

        // Очищаем массивы с заголовками, фото и ссылками перед новым поисковым запросом
        if (!arrayHeaders.isEmpty())   { arrayHeaders.clear(); }
        if (!arrayHrefs.isEmpty())       { arrayHrefs.clear(); }
        if (!arrUrlImg.isEmpty())       { arrUrlImg.clear(); }
        if (!arrMP4links.isEmpty())       { arrMP4links.clear(); }

        // Поисковый запрос без /http. По этим поисковым словам будем выдергивать видео из поиска сайта с видео
        String messageText = message.getText();
        String searchingMessage = messageText.replaceAll("/find", "");
        try {
            arrayHeaders = new HeadersSearch().getHeadersOfVideos(searchingMessage);
            arrayHrefs = new HrefsWebpagesSearch().getHrefsOfVideos(searchingMessage);
            arrUrlImg = new ImagesSearch().getImages(searchingMessage);
            arrMP4links = new MP4Search().getHrefsOfMP4(searchingMessage);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Возвращает массив объектов SendingPhoto, которые в основном классе бота могут быть выполнены командой execute
    public ArrayList<SendPhoto> findCommand(Message message) throws TelegramApiException {

        ArrayList<SendPhoto> arrayListSendPhoto = new ArrayList<SendPhoto>();

        // Метод возвращает массивы данных по нашему запросу (заголовки, ссылки на видео и картинки)
        setArraysData(message);

        for (int i=0; i < 5; i++) {

            String caption = arrayHeaders.get(i);
            String href = arrayHrefs.get(i);
            String urlImg = arrUrlImg.get(i);
            String urlMP4 = arrMP4links.get(i);

            SendPhoto sendPhoto = new SendingPhoto().sendPhoto(message, caption, href, urlImg); // Отправляем фото с подписью и ссылкой
            arrayListSendPhoto.add(sendPhoto);

            // new DownloaderMP4().saveMP4(urlMP4, caption); // Метод сохраняет видео MP4 по переданной ссылке и дает название ролику 'caption'
        }

        return arrayListSendPhoto;
    }


    // Перегруженный метод поиска видео (с двумя параметрами). Второй параметр введен для поиска видео после получения CallbackQuery().getData()
    public ArrayList<SendPhoto> findCommand(Message message, String keywordText) {

        ArrayList<SendPhoto> arrayListSendPhoto = new ArrayList<SendPhoto>();

        // Очищаем массивы с заголовками, фото и ссылками перед новым поисковым запросом
        if (!arrayHeaders.isEmpty())   { arrayHeaders.clear(); }
        if (!arrayHrefs.isEmpty())       { arrayHrefs.clear(); }
        if (!arrUrlImg.isEmpty())       { arrUrlImg.clear(); }
        if (!arrMP4links.isEmpty())       { arrMP4links.clear(); }

        // Поисковый запрос без /http. По этим поисковым словам будем выдергивать видео из поиска сайта с видосами
        String searchingMessage = keywordText;
        System.out.println(searchingMessage);

        // Отправим клиенту ссылки на видео
        try {

            arrayHeaders = new HeadersSearch().getHeadersOfVideos(searchingMessage);
            arrayHrefs = new HrefsWebpagesSearch().getHrefsOfVideos(searchingMessage);
            arrUrlImg = new ImagesSearch().getImages(searchingMessage);
            arrMP4links = new MP4Search().getHrefsOfMP4(searchingMessage);

            //lastIdOfVideo = 5;

            for (int i=0; i < 5; i++) {

                String caption = arrayHeaders.get(i);
                String href = arrayHrefs.get(i);
                String urlImg = arrUrlImg.get(i);
                String urlMP4 = arrMP4links.get(i);

                SendPhoto sendPhoto = new SendingPhoto().sendPhoto(message, caption, href, urlImg); // Отправляем фото с подписью и ссылкой
                arrayListSendPhoto.add(sendPhoto);

                new DownloaderMP4().saveMP4(urlMP4, caption); // Метод сохраняет видео MP4 по переданной ссылке и дает название ролику 'caption'
                System.out.println("Заголовок - " + caption);
                System.out.println("Поисковое слово - "+searchingMessage);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        return arrayListSendPhoto;
    }

    public ArrayList<String> getArrayHeaders() {
        return arrayHeaders;
    }

    public ArrayList<String> getArrayHrefs() {
        return arrayHrefs;
    }

    public ArrayList<String> getArrUrlImg() {
        return arrUrlImg;
    }

    public ArrayList<String> getArrMP4links() {
        return arrMP4links;
    }
}
