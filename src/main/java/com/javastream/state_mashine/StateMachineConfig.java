package com.javastream.state_mashine;

import com.javastream.states.OrderEvents;
import com.javastream.states.OrderStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
        config.withConfiguration().autoStartup(true); // Старт стейтмашины при создании новой машины
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states.withStates()
                .initial(OrderStates.FIND)
                .state(OrderStates.MORE)
                .end(OrderStates.ALL);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStates.START)
                .target(OrderStates.FIND)
                .event(OrderEvents.FOUND_COMMAND)


                .and()
                .withExternal()
                .source(OrderStates.FIND)
                .target(OrderStates.MORE)
                .event(OrderEvents.MORE_COMMAND)
                .action(findCommand())

                .and()
                .withExternal()
                .source(OrderStates.MORE)
                .target(OrderStates.ALL)
                .event(OrderEvents.ALL_COMMAND);

    }


    private Action<OrderStates,OrderEvents> findCommand() {
        return new Action<OrderStates,OrderEvents>() {

            @Override
            public void execute(StateContext<OrderStates,OrderEvents> context) {
                // do something
                System.out.println("Выполнение команды - > FIND");
            }
        };
    }


}
