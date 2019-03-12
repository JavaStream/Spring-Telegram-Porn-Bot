package com.javastream.states;

public enum OrderEvents {
    FOUND_COMMAND,
    MORE_COMMAND,
    ALL_COMMAND
    ;


    public String getFoundCommandValue() {
        return "/found";
    }
}
