package com.javastream;

import com.javastream.commands.Search;
import com.javastream.model.Sender;
import com.javastream.model.Videos;
import com.javastream.service.InlineKeyboard;
import com.javastream.service.SendingPhoto;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import com.javastream.util.MessegeTextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static java.lang.Math.toIntExact;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);

    private StateMachine<OrderStates, OrderEvents> stateMachine;

    @Autowired
    private MachineBuilder machineBuilder;

    @Autowired
    MessegeTextUtil messegeTextUtil;

    @Autowired
    private Search search;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;



    public VideoBot() {
    }

    public void startStateMachine() throws Exception {
        stateMachine = machineBuilder.buildMachine();
        stateMachine.start();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && !update.hasCallbackQuery()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                String messageText = message.getText();

                // Передаем стейт машине событие, которое запросил пользователь и обьект message
                OrderEvents event = messegeTextUtil.getEvent(messageText);
                stateMachine.getExtendedState().getVariables().put("message", message);
                stateMachine.getExtendedState().getVariables().put("update", update);
                stateMachine.sendEvent(event);

                // Отправляем в чат данные, полученные от стейт-машины
                executeMessage();
                /*
                try {
                    execute(new InlineKeyboard().send(update)); // Вызываем инлайн клаву
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                */
            }
        }
        else if (update.hasCallbackQuery()) {

            System.out.println("Вызван колбэк");
            // Определяем данные колбэка (текст, id сообщения и чата)
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            Message message = update.getCallbackQuery().getMessage();
            OrderEvents event = messegeTextUtil.getEvent(call_data);

            System.out.println("call_data = " + call_data);

            if (call_data != null) {
                // заменяем исходный текст после нажатия на кнопку на новый текст
                String answer = "Ваш запрос выполняется..";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);

                try {
                    // отправляем новый текст в чат
                    execute(new_message); //
                    stateMachine.getExtendedState().getVariables().put("message", message);
                    stateMachine.getExtendedState().getVariables().put("callData", call_data);
                    stateMachine.sendEvent(event);

                    executeMessage();
                    // Отправить стейт-машине событие - колбэк, которое она обработает и вернет в видео

                    //findCommand(update.getCallbackQuery().getMessage(), update.getCallbackQuery().getData());

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }



        }

    }



    /*
    *  Универсальный метод по выводу в чат данного разного типа
    *  Метод getExcecuteMethod() статического класс Sender возвращает тип обьекта, исходя из которого
    *  выполнение будет передано соответствующему executeMessage()
    */
    private void executeMessage() {
        if (Sender.getExcecuteMethod().equals("sendMessage")) {
            executeMessage(Sender.getSendMessage());
        }
        else if (Sender.getExcecuteMethod().equals("arrayListSendPhoto")) {
            executeMessage(Sender.getArrayListSendPhoto());
        }
        else if (Sender.getExcecuteMethod().equals("arrayVideo")) {
            executeMessage(Sender.getVideos());
        }
    }



    public void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(ArrayList<SendPhoto> photoList) {
        try {
            for(SendPhoto sendPhoto : photoList)
                execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(Videos videos) {
        for (int i = 0; i < videos.getArrayCaptions().size(); i++) {
            try {
                execute(new SendingPhoto().sendPhoto(videos.getMessage().get(i), videos.getArrayCaptions().get(i), videos.getArrayHref().get(i), videos.getArrUrlImg().get(i)));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }


    @PostConstruct
    public void start() throws Exception {
        startStateMachine();
        logger.info("username: {}, token: {}", username, token);
    }


}




