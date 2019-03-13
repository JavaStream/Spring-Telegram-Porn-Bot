package com.javastream.service;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Отправка инлайн-клавиатуры
 */
public class InlineKeyboard {

    public SendMessage send(Update update) {

        String categorieText = "Выберите категорию из представленных ниже"; // к этому сообщению в чате будут прикреплены все кнопки

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Создаем объект разметки клавиатуры

        // Создаем кнопки
        InlineKeyboardButton inlineBtn_1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineBtn_2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineBtn_3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineBtn_4 = new InlineKeyboardButton();

        // Прикрепляем к ним текст на кнопке и колбэки (отклики на нажатие)
        inlineBtn_1.setText("Ass").setCallbackData("ass");
        inlineBtn_2.setText("Asian").setCallbackData("asian");
        inlineBtn_3.setText("German").setCallbackData("german");
        inlineBtn_4.setText("Russian").setCallbackData("russian");

        // Создаем массив строк, в которые будут помещены те или иные кнопки
        List<InlineKeyboardButton> keybRow1 = new ArrayList<InlineKeyboardButton>();

        // Помещаем в строки внопки
        keybRow1.add(inlineBtn_1);
        keybRow1.add(inlineBtn_2);
        keybRow1.add(inlineBtn_3);
        keybRow1.add(inlineBtn_4);

        // Создаем массив всех рядов кнопок
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keybRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(categorieText);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;

    }


}