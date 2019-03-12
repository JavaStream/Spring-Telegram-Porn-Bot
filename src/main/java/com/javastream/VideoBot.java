package com.javastream;

import com.javastream.service.SendTextMsg;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
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

    public VideoBot(StateMachine<OrderStates, OrderEvents> stateMachine) throws Exception {
        this.stateMachine = new MachineBuilder().buildMachine();
        stateMachine.start();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String messageText = message.getText();
            // Команда find - поиск ссылок на видео с постерами по ключевым словам пользователя

            stateMachine.sendEvent(OrderEvents.FOUND_COMMAND);

            if (messageText.contains("/find")) {
                stateMachine.sendEvent(OrderEvents.MORE_COMMAND);

                System.out.println("Команда -- find -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- find -- отработала"));
            }

            if (messageText.contains("/more")) {
                stateMachine.sendEvent(OrderEvents.ALL_COMMAND);
                System.out.println("Команда -- more -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- more -- отработала"));
            }

            if (messageText.contains("/all")) {
                System.out.println("Команда -- all -- отработала");
                executeMessage(new SendTextMsg().sendTextMsg(message, "Команда -- all -- отработала"));
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

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
