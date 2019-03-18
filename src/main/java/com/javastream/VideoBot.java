package com.javastream;

import com.javastream.model.Sender;
import com.javastream.model.Videos;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);

    private StateMachine<OrderStates, OrderEvents> stateMachine;

    @Autowired
    private MachineBuilder machineBuilder;

    @Autowired
    MessegeTextUtil messegeTextUtil;

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
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                String messageText = message.getText();

                // Передаем стейт машине событие, которое запросил пользователь и обьект message
                OrderEvents event = messegeTextUtil.getEvent(messageText);
                stateMachine.getExtendedState().getVariables().put("message", message);
                stateMachine.sendEvent(event);


                /* Если статический класс Sender не содержит массив типа SendPhoto(), то выполнение
                *  передать методу executeMessage(SendMessage sendMessage), в противном случае должен
                *  быть вызван метод executeMessage(ArrayList<SendPhoto> photoLis)
                *  (!) Попробовать реализовать без блока IF ELSE за счет перегрузки метода executeMessage()
                */


                if (Sender.getExcecuteMethod().equals("sendMessage")) {
                    executeMessage(Sender.getSendMessage());
                }
                else if (Sender.getExcecuteMethod().equals("arrayListSendPhoto"))  {
                    executeMessage(Sender.getArrayListSendPhoto());
                }
                else if (Sender.getExcecuteMethod().equals("arrayVideo")) {
                    System.out.println("arrayVideo");
                    System.out.println("getArrUrlImg().size() - " + Sender.getVideos().getArrUrlImg().size());
                    System.out.println("getArrayCaptions().size() - " + Sender.getVideos().getArrayCaptions().size());
                    System.out.println("getArrayHref().size() - " + Sender.getVideos().getArrayHref().size());
                    System.out.println("getUrlMP4().size() - " + Sender.getVideos().getUrlMP4().size());
                    executeMessage(Sender.getVideos());
                }


            }
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
    public void start() throws Exception {
        startStateMachine();
        logger.info("username: {}, token: {}", username, token);
    }


}




