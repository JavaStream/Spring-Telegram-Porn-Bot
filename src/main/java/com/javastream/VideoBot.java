package com.javastream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import com.javastream.util.MessegeTextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private StateMachine<OrderStates, OrderEvents> stateMachine;

    @Autowired
    private MachineBuilder machineBuilder;


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
                stateMachine.sendEvent(event);
/*
                if (stateMachine.getState().getId().name().equals("START")) {

                    System.out.println("Добро пожаловать в чат-бот!");
                }

                if (stateMachine.getState().getId().name().equals("FIND")) {
                    logger.info("200. Current State -> {}", stateMachine.getState().getId().name());
                    new StateFinder().getMethodOfEvent( stateMachine.getState().getId().name(), message);
                } else {
                    System.out.println("Другая команда выполняется");
                }

*/














/*
            if (stateMachine.getState().getId().name().toString().equals("FIND")) {
                System.out.println("0.1.put message ");
                stateMachine.getExtendedState().getVariables().put("message", message);
                System.out.println("0.2.put message ");
            }

            System.out.println("1.stateMachine.getState().getId().name().toString() - " + stateMachine.getState().getId().name().toString());
            System.out.println("2.stateMachine.getState().getId().name().toString()" + stateMachine.getState().getId().name().toString());
            executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- "+stateMachine.getState().getId().name().toString()+ "-- отработала"));
*/

            /*
            if (stateMachine.getState().getId().name().toString() == "FIND") {

                this.machineBuilder.find();
            }

            */
            }

        }



    }


    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }




    public void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
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
}
