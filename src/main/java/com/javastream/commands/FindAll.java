package com.javastream.commands;

import com.javastream.service.SendingPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

@Component
public class FindAll {

    @Autowired
    private Search search;

    @Autowired
    private SendingPhoto sendingPhoto;

    private ArrayList<SendPhoto> arrayFullListSendPhoto;    // Полный список видео по запросу юзера

    public FindAll() {
        arrayFullListSendPhoto  = new ArrayList<SendPhoto>();
    }


    // Создание полного массива роликов по запросу юзера
    public ArrayList<SendPhoto> init() {
        for (int i=0; i < search.getHeadersSearchList().size(); i++) {
            String caption = search.getHeadersSearchList().get(i);
            String href = search.getHrefsWebpagesSearchList().get(i);
            String urlMP4 = search.getMp4SearchList().get(i);

            SendPhoto sendPhoto = sendingPhoto.sendPhoto(search.getMessage(), caption, href); // Отправляем фото с подписью и ссылкой
            arrayFullListSendPhoto.add(sendPhoto);
        }

        return arrayFullListSendPhoto;
    }
}
