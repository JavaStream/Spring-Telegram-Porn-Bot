package com.javastream.state_mashine;

import com.javastream.VideoBot;
import com.javastream.commands.Find;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.EnumSet;

@Component
public class MachineBuilder {

    private static final Logger logger = LoggerFactory.getLogger(VideoBot.class);

       public StateMachine<OrderStates, OrderEvents> buildMachine() throws Exception {
        Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                .initial(OrderStates.START)
                .states(EnumSet.allOf(OrderStates.class));

        builder.configureTransitions()
                .withExternal()
                .source(OrderStates.START).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)
                .action(start())

                .and()
                .withExternal()
                .source(OrderStates.START).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(find())

                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(find())


                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.MORE)
                .event(OrderEvents.MORE_COMMAND)

                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.START)
                .event(OrderEvents.START_COMMAND)

                .and()
                .withExternal()
                .source(OrderStates.FIND).target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND)

                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND)

                .and()
                .withExternal()
                .source(OrderStates.MORE).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)

                .and()
                .withExternal()
                .source(OrderStates.ALL).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND);

        return builder.build();
    }



    public Action<OrderStates,OrderEvents> find() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // do something
                System.out.println("10.1." + "Выполнение команды в action - > FIND");
                logger.info("300. Current State -> {}", context.getEvent().name());

                // Message message = context.getExtendedState().get("message", Message.class);

               //     System.out.println("11.0");
                //    System.out.println("11.1." + context.getExtendedState().get("message", Message.class));
                    //System.out.println("11.2." + context.getExtendedState().get("message", Message.class).getText());


                //new Find().findCommand(context.getExtendedState().get("message", Message.class));
            }
        };
    }


    public Action<OrderStates,OrderEvents> start() {
        return new Action<OrderStates,OrderEvents>() {
            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // do something
                logger.info("200. Current State -> {}", context.getEvent().name());
                System.out.println("20.1." + "Выполнение команды в action - > START");

                // Message message = context.getExtendedState().get("message", Message.class);

                //     System.out.println("11.0");
                //    System.out.println("11.1." + context.getExtendedState().get("message", Message.class));
                //System.out.println("11.2." + context.getExtendedState().get("message", Message.class).getText());


                //new Find().findCommand(context.getExtendedState().get("message", Message.class));
            }
        };
    }


}
