package com.javastream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.javastream.state_mashine.MachineBuilder;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import com.javastream.util.MessegeTextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

public class Print {

    public void printing(Message message) {

        //videoBot.executeMessage(sendTextMsg.sendTextMsg(message, "1111111111 -- ТЕСТ ПРОЙДЕН!"));
        //this.videoBot.printTrace();
        System.out.println("1111111111 -- ТЕСТ ПРОЙДЕН!");

    }


}
