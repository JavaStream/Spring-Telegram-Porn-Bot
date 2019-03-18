package com.javastream.util;

import com.javastream.states.OrderEvents;
import org.springframework.stereotype.Component;

/**
 *  Получает название команды без слэша и в верхнем регистре, переводит к формату событий (добавляет подпись _COMMAND)
 *  На выходе выдает: START_COMMAND, FIND_COMMAND  и т.д.
 */

@Component
public class MessegeTextUtil {

    private OrderEvents event;
    private String serchingText;

    // Приводит команду к нужному формату
    public String getSerchingText(String message) {
        String[] arr = message.split(" ");
        String messegeTextWithoutSlash = arr[0].replace("/", "").toUpperCase(); // Without Slash and Upper Case
        serchingText = messegeTextWithoutSlash + "_COMMAND";
        return serchingText;
    }

    // Находит запрошенное событие в OrderEvents
    public OrderEvents getEvent(String message) {
        getSerchingText(message);
        this.event = OrderEvents.valueOf(serchingText);
        return event;
    }


}
