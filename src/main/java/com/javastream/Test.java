package com.javastream;

import org.springframework.stereotype.Component;

@Component
public class Test {

    public String print(String text) {
        return text + "!";
    }

}
