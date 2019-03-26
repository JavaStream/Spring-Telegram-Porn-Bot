package com.javastream.state_mashine;

import com.javastream.VideoBot;
import com.javastream.commands.FindAll;
import com.javastream.commands.FindPart;
import com.javastream.commands.Search;
import com.javastream.service.InlineKeyboard;
import com.javastream.service.Properties;
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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.EnumSet;

@Component
public class MachineBuilder {

    @Autowired
    private Search search;

    @Autowired
    private FindPart findPart;

    @Autowired
    private FindAll findAll;

    @Autowired
    private SendTextMsg sendTextMsg;

    private Sender sender;
    private int lastIdOfVideo = 0;
    ArrayList<SendPhoto> arrayFullListSendPhoto;
    private final int MORE_PART_VIDEOS = Properties.NUMBER_OF_VIDEOS;

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

            // START -> CATALOG
            .and().withExternal()
            .source(OrderStates.START).target(OrderStates.CATALOG)
            .event(OrderEvents.CATALOG_COMMAND)
            .action(catalog())

            // CATALOG - > FIND  (ASS callback)
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.AMERICAN_COMMAND)
            .action(findCallback())

            // CATALOG - > FIND  (GERMANY callback)
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.GERMANY_COMMAND)
            .action(findCallback())

            // CATALOG - > FIND  (FRANCE callback)
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.FRANCE_COMMAND)
            .action(findCallback())

            // CATALOG - > FIND  (RUSSIAN callback)
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.RUSSIAN_COMMAND)
            .action(findCallback())

            // CATALOG -> FIND
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(find())

            // FIND -> CATALOG
            .and().withExternal()
            .source(OrderStates.FIND).target(OrderStates.CATALOG)
            .event(OrderEvents.CATALOG_COMMAND)
            .action(catalog())

            // MORE -> CATALOG
            .and().withExternal()
            .source(OrderStates.MORE).target(OrderStates.CATALOG)
            .event(OrderEvents.CATALOG_COMMAND)
            .action(catalog())

            // ALL -> CATALOG
            .and().withExternal()
            .source(OrderStates.ALL).target(OrderStates.CATALOG)
            .event(OrderEvents.CATALOG_COMMAND)
            .action(catalog())

            // CATALOG -> FIND
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(find())

            // CATALOG -> START
            .and().withExternal()
            .source(OrderStates.CATALOG).target(OrderStates.START)
            .event(OrderEvents.START_COMMAND)
            .action(start())

            // START -> FIND
            .and().withExternal()
            .source(OrderStates.START).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(find())

            // FIND
            .and().withExternal()
            .source(OrderStates.FIND).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(find())


            // FIND -> START
            .and().withExternal()
            .source(OrderStates.FIND).target(OrderStates.START)
            .event(OrderEvents.START_COMMAND)
            .action(start())

            // FIND -> MORE
            .and().withExternal()
            .source(OrderStates.FIND).target(OrderStates.MORE)
            .event(OrderEvents.MORE_COMMAND)
            .action(more())

            // MORE -> FIND
            .and().withExternal()
            .source(OrderStates.MORE).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(find())

            // MORE -> MORE
            .and().withExternal()
            .source(OrderStates.MORE).target(OrderStates.MORE)
            .event(OrderEvents.MORE_COMMAND)
            .action(more())

            // MORE -> START
            .and().withExternal()
            .source(OrderStates.MORE).target(OrderStates.START)
            .event(OrderEvents.START_COMMAND)
            .action(start())

            // FIND -> ALL
            .and().withExternal()
            .source(OrderStates.FIND).target(OrderStates.ALL)
            .event(OrderEvents.ALL_COMMAND)
            .action(findAll())

            // MORE -> ALL
            .and().withExternal()
            .source(OrderStates.MORE).target(OrderStates.ALL)
            .event(OrderEvents.ALL_COMMAND)
            .action(findAll())

            // ALL -> FIND
            .and().withExternal()
            .source(OrderStates.ALL).target(OrderStates.FIND)
            .event(OrderEvents.FIND_COMMAND)
            .action(findAll())

            // ALL -> START
            .and().withExternal()
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
                SendMessage sendMessage = sendTextMsg.send(message, "Для поиска  видео задайте команду /find и поисковый запрос. Например, /find hot girls или /find горячие девчонки. Для просмотра меню наберите команду /catalog");

                sender.setSendMessage(sendMessage);
                sender.setExcecuteMethod("sendMessage");
            }
        };
    }


    // START()
    public Action<OrderStates,OrderEvents> catalog() {

        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                /* Результаты выполнения данного метода будут записаны в полt статического класса Sender, а затем
                *  получены в классе VideoBot
                */
                logger.info("150. Current State -> {}", context.getEvent().name());
                Message message = context.getExtendedState().get("message", Message.class);
                Update update = context.getExtendedState().get("update", Update.class);

                SendMessage sendMessage = new InlineKeyboard().send(update);
                sender.setSendMessage(sendMessage);
                sender.setExcecuteMethod("sendMessage");
            }
        };
    }


    // FIND()
    public Action<OrderStates,OrderEvents> findCallback() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // Поиск видеороликов и выбор первой порции для отправки их юзеру
                logger.info("400. Current Event -> {}", context.getEvent().name());

                Message message = context.getExtendedState().get("message", Message.class);
                Update update = context.getExtendedState().get("update", Update.class);

                String callData = context.getExtendedState().get("callData", String.class);

                logger.info("401. Serching message -> {}", callData);

                search.init(message, callData);
                sender.setArrayListSendPhoto(findPart.init(lastIdOfVideo));
                sender.setExcecuteMethod("arrayListSendPhoto");

            }
        };
    }

    // FIND()
    public Action<OrderStates,OrderEvents> find() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // Поиск видеороликов и выбор первой порции для отправки их юзеру
                logger.info("200. Current Event -> {}", context.getEvent().name());

                Message message = context.getExtendedState().get("message", Message.class);
                logger.info("201. Serching message -> {}", message.getText());

                search.init(message);
                sender.setArrayListSendPhoto(findPart.init(lastIdOfVideo));
                sender.setExcecuteMethod("arrayListSendPhoto");
            }
        };
    }


    // MORE()
    private Action<OrderStates,OrderEvents> more() {
        return new Action<OrderStates, OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates, OrderEvents> context) {
                // Вывод видеоконтента порциями. Кол-во роликов задается юзером в Properties
                logger.info("300. Current State -> {}", context.getEvent().name());

                lastIdOfVideo += MORE_PART_VIDEOS;
                ArrayList<SendPhoto> arrayPartListSendPhoto = findPart.init(lastIdOfVideo);
                Sender.setArrayListSendPhoto(arrayPartListSendPhoto);
                sender.setExcecuteMethod("arrayListSendPhoto");
            }
        };
    }


    // findAll()
    private Action<OrderStates,OrderEvents> findAll() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // Find all videos for user's current request.
                logger.info("400. Current State -> {}", context.getEvent().name());
                arrayFullListSendPhoto = new ArrayList<SendPhoto>();

                arrayFullListSendPhoto = findAll.init();
                sender.setArrayListSendPhoto(arrayFullListSendPhoto);
                sender.setExcecuteMethod("arrayListSendPhoto");
            }
        };
    }
}
