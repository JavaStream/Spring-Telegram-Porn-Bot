package com.javastream;

import com.javastream.model.Sender;
import com.javastream.service.SendTextMsg;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import com.javastream.util.MessegeTextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private StateMachine<OrderStates, OrderEvents> stateMachine;

    public VideoBot() throws Exception {
       this.stateMachine = new MachineBuilder().buildMachine();
       this.stateMachine.start();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                String messageText = message.getText();

                MessegeTextUtil messegeTextUtil = new MessegeTextUtil(messageText);

                // Передаем стейт машине событие, которое запросил пользователь
                OrderEvents event = messegeTextUtil.getEvent();
                stateMachine.getExtendedState().getVariables().put("message", message);
                stateMachine.sendEvent(event);

                /* Если статический класс Sender не содержит массив типа SendPhoto(), то выполнение
                *  передать методу executeMessage(SendMessage sendMessage), в противном случае должен
                *  быть вызван метод executeMessage(ArrayList<SendPhoto> photoLis)
                */
                if (Sender.getExsistArray() == false) {
                    SendMessage sendMessage = new SendTextMsg().sendTextMsg(Sender.getMessage(), Sender.getText());
                    executeMessage(sendMessage);
                } else {
                    executeMessage(Sender.getArrayListSendPhoto());
                }
            }
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


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }


}




