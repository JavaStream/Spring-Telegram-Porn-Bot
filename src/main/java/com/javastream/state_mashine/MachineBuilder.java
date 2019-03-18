package com.javastream.state_mashine;

import com.javastream.VideoBot;
import com.javastream.commands.FindNew;
import com.javastream.service.Properties;
import com.javastream.commands.MoreSelect;
import com.javastream.model.Sender;
import com.javastream.service.SendTextMsg;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.EnumSet;

@Component
public class MachineBuilder {

    @Autowired
    private FindNew findNew;

    @Autowired
    private SendTextMsg sendTextMsg;

    @Autowired
    private MoreSelect moreSelect;


    private Sender sender;
    ArrayList<String> arrayHeaders;
    ArrayList<String> arrayHrefs;
    ArrayList<String> arrUrlImg;
    ArrayList<String> arrMP4links;
    private int lastIdOfVideo = Properties.NUMBER_OF_VIDEOS_MORE;


    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);


       public StateMachine<OrderStates, OrderEvents> buildMachine() throws Exception {
        Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                .initial(OrderStates.START)
                .states(EnumSet.allOf(OrderStates.class));

        builder.configureTransitions()

                // START
                .withExternal()
                .source(OrderStates.START).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)
                .action(start())

                // START -> FIND
                .and()
                .withExternal()
                .source(OrderStates.START).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(find())

                // FIND
                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(find())

                // FIND -> START
                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)
                .action(start())

                // FIND -> MORE
                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.MORE)
                .event(OrderEvents.MORE_COMMAND)
                .action(more())

                // MORE -> FIND
                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(find())

                // MORE -> MORE
                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.MORE)
                .event(OrderEvents.MORE_COMMAND)
                .action(more())

                // MORE -> START
                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)
                .action(start())

                // FIND -> ALL
                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND)

                // MORE -> ALL
                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND)

                // ALL -> FIND
                .and()
                .withExternal()
                .source(OrderStates.ALL).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND);

        return builder.build();
    }

    // START()
    public Action<OrderStates,OrderEvents> start() {

        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                /* Результаты выполнения данного метода будут записаны в полt статического класса Sender, а затем
                *  получены в классе VideoBot
                */
                logger.info("100. Current State -> {}", context.getEvent().name());
                Message message = context.getExtendedState().get("message", Message.class);
                SendMessage sendMessage = sendTextMsg.sendTextMsg(message, "Для поиска  видео задайте команду /find и поисковый запрос. Например, /find hot girls или /find горячие девчонки");

                sender.setSendMessage(sendMessage);
                sender.setExcecuteMethod("sendMessage");
            }
        };
    }


    // FIND()
    public Action<OrderStates,OrderEvents> find() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // FIND Method
                logger.info("200. Current State -> {}", context.getEvent().name());

                Message message = context.getExtendedState().get("message", Message.class);

                logger.info("201. Serching for -> {}", message.getText());

                try {
                    sender.setArrayListSendPhoto(findNew.findCommand(message));
                    sender.setExcecuteMethod("arrayListSendPhoto");

                    arrayHeaders = new ArrayList<String>();
                    arrayHrefs = new ArrayList<String>();
                    arrUrlImg = new ArrayList<String>();
                    arrMP4links = new ArrayList<String>();

                    arrayHeaders = findNew.getHeadersSearchList();
                    arrayHrefs = findNew.getHrefsWebpagesSearchList();
                    arrUrlImg = findNew.getImagesSearchList();
                    arrMP4links = findNew.getMp4SearchList();

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // MORE()
    private Action<OrderStates,OrderEvents> more() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // More send video. It calls MoreSelect Command.
                logger.info("300. Current State -> {}", context.getEvent().name());

                Message message = context.getExtendedState().get("message", Message.class);

                logger.info("301. More sending -> {}", message.getText());
                //findNew.setArraysData(message);

                sender.setExcecuteMethod("arrayVideo");
                sender.setVideos(moreSelect.outputMore(message, arrayHeaders, arrayHrefs, arrUrlImg, arrMP4links, lastIdOfVideo));
                sender.setMessage(message);
                lastIdOfVideo += lastIdOfVideo;
            }
        };
    }

}
