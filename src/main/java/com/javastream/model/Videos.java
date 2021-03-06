package com.javastream.model;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

/**
 * Created by javastream on 26.02.2019.
 */

@Component
public class Videos {

    private ArrayList<Message> message;
    private ArrayList<String> arrayCaptions;
    private ArrayList<String> arrayHref;
    private ArrayList<String> arrUrlImg;
    private ArrayList<String> urlMP4;
    //private String endOfArray;

    public Videos() {
        message = new ArrayList<Message>();
        arrayCaptions = new ArrayList<String>();
        arrayHref = new ArrayList<String>();
        arrUrlImg = new ArrayList<String>();
        urlMP4 = new ArrayList<String>();
    }

    public ArrayList<Message> getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message.add(message);
    }

    public ArrayList<String> getArrayCaptions() {
        return arrayCaptions;
    }

    public void setArrayCaptions(String arrayCaptions) {
        this.arrayCaptions.add(arrayCaptions);
    }

    public ArrayList<String> getArrayHref() {
        return arrayHref;
    }

    public void setArrayHref(String arrayHref) {
        this.arrayHref.add(arrayHref);
    }

    public ArrayList<String> getArrUrlImg() {
        return arrUrlImg;
    }

    public void setArrUrlImg(String arrUrlImg) {
        this.arrUrlImg.add(arrUrlImg);
    }

    public ArrayList<String> getUrlMP4() {
        return urlMP4;
    }

    public void setUrlMP4(String urlMP4) {
        this.urlMP4.add(urlMP4);
    }

}