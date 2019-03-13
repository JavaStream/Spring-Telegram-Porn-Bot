package com.javastream.state_mashine;

import com.javastream.commands.Find;
import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class MachineBuilder {

       public StateMachine<OrderStates, OrderEvents> buildMachine() throws Exception {
        Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                .initial(OrderStates.START)
                .states(EnumSet.allOf(OrderStates.class));

        builder.configureTransitions()
                .withExternal()
                .source(OrderStates.START).target(OrderStates.FIND)
                .event(OrderEvents.FIND_COMMAND)
                .action(new Find().find())


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

}