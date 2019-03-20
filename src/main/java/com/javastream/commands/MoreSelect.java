package com.javastream.commands;

import com.javastream.model.Videos;
import com.javastream.service.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

/**
 * Отвечает за выдачу очередной порции роликов или фото по запросу пользователя
 */
@Component
public class MoreSelect {

    int more = 0;
    int countOfVideo = Properties.NUMBER_OF_VIDEOS_MORE; // кол-во роликов на запрос /more от пользователя


    @Autowired
    private Videos videos;

    public MoreSelect() {

    }

    public Videos outputMore(Message message, ArrayList<String> arrayCaptions, ArrayList<String> arrayHref, ArrayList<String> arrUrlImg, ArrayList<String> urlMP4, Integer lastIdOfVideo) {

        // i присваиваем последний индекс в массиве с видео. А дальше выбираем следующую выборку видео (равную countOfVideo)
        for (int i = lastIdOfVideo; i < (countOfVideo+lastIdOfVideo); i++) {

            if (i < arrUrlImg.size())  {
                videos.setArrayCaptions(arrayCaptions.get(i));
                videos.setArrayHref(arrayHref.get(i));
                videos.setArrUrlImg(arrUrlImg.get(i));
                videos.setMessage(message); // СЮДА только один ID месседжа заходит а не массив! В одно и тоже сообщение в БОТ нельзя впихнуть много сообщений

                // Добавляем в массив данные по ролику
                System.out.println("счетчик - " + i);
                //new DownloaderMP4().saveMP4(urlMP4.get(i), arrayCaptions.get(i)); // Метод сохраняет видео MP4 по переданной ссылке и дает название ролику 'caption'
            }


            // Если индекс равен размеру массива, т.е. массив с видео исчерпан, то предлагаем пользователю начать поиск по новым ключевым словам
            if (i == arrUrlImg.size()) {
                System.out.println("Введите команду /find");
                videos.setEndOfArray("end_video_array");
            }



            // Переменная для определения последнего элемента после того как цикл отработал
            more = i;
        }

        return videos;
    }

}