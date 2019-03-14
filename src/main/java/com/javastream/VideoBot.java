package com.javastream;

import com.javastream.service.SendTextMsg;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import com.javastream.util.MessegeTextUtil;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final String BOT_NAME = "PornHunterbot";
    private static final String BOT_TOKEN = "660690556:AAGFTyOBivpOWWc_S87WUYU0BH9sQDqUP0M";

    private StateMachine<OrderStates, OrderEvents> stateMachine;

    public VideoBot() throws Exception {
       this.stateMachine = new MachineBuilder().buildMachine();
       this.stateMachine.start();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String messageText = message.getText();

            MessegeTextUtil messegeTextUtil = new MessegeTextUtil(messageText);

            // Передаем стейт машине событие, которое запросил пользователь
            OrderEvents event = messegeTextUtil.getEvent();
            stateMachine.sendEvent(event);
            //stateMachine.getExtendedState().getVariables().put("message", message);


            System.out.println(stateMachine.getState().getId().name().toString());
            executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- "+stateMachine.getState().getId().name().toString()+ "-- отработала"));

/*
            if (messageText.contains("/start")) {

                System.out.println("Команда -- start -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- start -- отработала"));
            }


            if (messageText.contains("/find")) {
                stateMachine.sendEvent(OrderEvents.FOUND_COMMAND);
                System.out.println(stateMachine.getState().getId().name().toString());

                System.out.println("Команда -- find -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- find -- отработала"));
            }

            if (messageText.contains("/more")) {
                stateMachine.sendEvent(OrderEvents.MORE_COMMAND);
                System.out.println(stateMachine.getState().getId().name().toString());
                System.out.println("Команда -- more -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- more -- отработала"));
            }

            if (messageText.contains("/all")) {
                stateMachine.sendEvent(OrderEvents.ALL_COMMAND);
                System.out.println(stateMachine.getState().getId().name().toString());
                System.out.println("Команда -- all -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- all -- отработала"));
            }

            */
        }


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
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
