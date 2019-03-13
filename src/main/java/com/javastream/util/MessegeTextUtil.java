package com.javastream.util;

import com.javastream.states.OrderEvents;

/**
 *  Получает название команды без слэша и в верхнем регистре, переводит к формату событий (добавляет подпись _COMMAND)
 */

public class MessegeTextUtil {

    private OrderEvents event;
    String serchingText;

    public MessegeTextUtil(String message) {
        String[] arr = message.split(" ");
        String messegeTextWithoutSlash = arr[0].replace("/", "").toUpperCase(); // Without Slash and Upper Case
        serchingText = messegeTextWithoutSlash + "_COMMAND";
    }

    // Приводит команду к нужному формату
    public String getSerchingText() {
        return serchingText;
    }

    // Находит запрошенное событие в OrderEvents
    public OrderEvents getEvent() {
        this.event = OrderEvents.valueOf(serchingText);
        return event;
    }


}
