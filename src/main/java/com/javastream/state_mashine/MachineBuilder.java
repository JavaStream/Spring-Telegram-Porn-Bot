package com.javastream.state_mashine;

import com.javastream.VideoBot;
import com.javastream.commands.Find;
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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.EnumSet;

@Component
public class MachineBuilder {

    @Autowired
    private Find find;

    @Autowired
    private SendTextMsg sendTextMsg;

    @Autowired
    private MoreSelect moreSelect;


    private Sender sender;
    ArrayList<String> arrayHeaders;
    ArrayList<String> arrayHrefs;
    ArrayList<String> arrUrlImg;
    ArrayList<String> arrMP4links;
    ArrayList<SendPhoto> arrayFullListSendPhoto;
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
                .action(findAll())

                // MORE -> ALL
                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND)
                .action(findAll())

                // ALL -> FIND
                .and()
                .withExternal()
                .source(OrderStates.ALL).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(findAll())

                // ALL -> START
                .and()
                .withExternal()
                .source(OrderStates.ALL).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)
                .action(start());

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
                SendMessage sendMessage = sendTextMsg.sendTextMsg(message, "Для поиска  видео задайте команду /find и поисковый запрос. Например, /find hot girls или /find горячие девчонки. Поиск может занять 20-30 сек.");

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


                    sender.setArrayListSendPhoto(find.findCommand(message));
                    sender.setExcecuteMethod("arrayListSendPhoto");

                    arrayHeaders = new ArrayList<String>();
                    arrayHrefs = new ArrayList<String>();
                    arrUrlImg = new ArrayList<String>();
                    arrMP4links = new ArrayList<String>();

                    arrayHeaders = find.getHeadersSearchList();
                    arrayHrefs = find.getHrefsWebpagesSearchList();
                    arrUrlImg = find.getImagesSearchList();
                    arrMP4links = find.getMp4SearchList();

            }
        };
    }

    // MORE()
    private Action<OrderStates,OrderEvents> more() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // More send video. It calls MoreSelect Command.
                Integer lastIdOfVideo = 4;
                logger.info("300. Current State -> {}", context.getEvent().name());

                Message message = context.getExtendedState().get("message", Message.class);

                logger.info("301. More sending -> {}", message.getText());
                sender.setExcecuteMethod("arrayVideo");
                sender.setVideos(moreSelect.outputMore(message, arrayHeaders, arrayHrefs, arrUrlImg, arrMP4links, lastIdOfVideo));
                sender.setMessage(message);
                lastIdOfVideo = lastIdOfVideo + lastIdOfVideo;
                // arrayHeaders.clear();
                // arrayHrefs.clear();
                // arrUrlImg.clear();
                // arrMP4links.clear();
                // lastIdOfVideo = lastIdOfVideo + lastIdOfVideo;
            }
        };
    }


    // findAll()
    private Action<OrderStates,OrderEvents> findAll() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // Find all videos for user's current requsts.
                logger.info("400. Current State -> {}", context.getEvent().name());
                Message message = context.getExtendedState().get("message", Message.class);
                arrayFullListSendPhoto = new ArrayList<SendPhoto>();

                arrayFullListSendPhoto = find.findAll(message);
                sender.setArrayListSendPhoto(arrayFullListSendPhoto);
                sender.setExcecuteMethod("arrayListSendPhoto");
            }
        };
    }

}
