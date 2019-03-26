package com.javastream.commands;

import com.javastream.service.Properties;
import com.javastream.service.SendingPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.ArrayList;

@Component
public class FindPart {

    @Autowired
    private Search search;

    @Autowired
    private SendingPhoto sendingPhoto;

    // Частичная выборка видео
    private ArrayList<SendPhoto> arrayPartListSendPhoto;

    public FindPart() {
        arrayPartListSendPhoto  = new ArrayList<SendPhoto>();
    }


    // Создание частичной выборки видео исходя из настроек юзера в Properties (выдача видео порциями)
    public ArrayList<SendPhoto> init(int lastOfIdVideo) {
        if (!arrayPartListSendPhoto.isEmpty())   { arrayPartListSendPhoto.clear(); }
        int count = Properties.NUMBER_OF_VIDEOS;

        for (int i = lastOfIdVideo; i < (count + lastOfIdVideo); i++) {
            String caption = search.getHeadersSearchList().get(i);
            String href = search.getHrefsWebpagesSearchList().get(i);
            String urlMP4 = search.getMp4SearchList().get(i);

            System.out.println(search.getHeadersSearchList().get(i));

            SendPhoto sendPhoto = sendingPhoto.sendPhoto(search.getMessage(), caption, href); // Формируем объект SendPfoto с подписью и ссылками
            arrayPartListSendPhoto.add(sendPhoto);
        }
        return arrayPartListSendPhoto;
    }
}
