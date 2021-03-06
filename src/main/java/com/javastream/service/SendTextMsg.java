package com.javastream.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SendTextMsg {

    public SendMessage send(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false); // при true вылетают исключения
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setParseMode("HTML");
        //sendMessage.setReplyToMessageId(message.getMessageId()); // подпись в заголовке поста (имя юзера)
        sendMessage.setText(s);

        return sendMessage;
    }

}