package com.javastream.model;

import com.javastream.service.SendTextMsg;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;


public class Sender {

    private static String text;
    private static Message message;
    private static SendMessage sendMessage;
    public static ArrayList<SendPhoto> arrayListSendPhoto;
    public static Videos videos;
    private static String excecuteMethod;


    public static SendMessage sendMessage() {
            return new SendTextMsg().send(Sender.getMessage(), Sender.getText());
    }

    public static ArrayList<SendPhoto> sendListSendPhoto() {
        return Sender.getArrayListSendPhoto();
    }

    public static String getText() {
        return text;
    }

    public static void setText(String text) {
        Sender.text = text;
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        Sender.message = message;
    }

    public static ArrayList<SendPhoto> getArrayListSendPhoto() {
        return arrayListSendPhoto;
    }

    public static void setArrayListSendPhoto(ArrayList<SendPhoto> arrayListSendPhoto) {
        Sender.arrayListSendPhoto = arrayListSendPhoto;
    }

    public static Videos getVideos() {
        return videos;
    }

    public static void setVideos(Videos videos) {
        Sender.videos = videos;
    }

    public static String getExcecuteMethod() {
        return excecuteMethod;
    }

    public static void setExcecuteMethod(String excecuteMethod) {
        Sender.excecuteMethod = excecuteMethod;
    }

    public static SendMessage getSendMessage() {
        return sendMessage;
    }

    public static void setSendMessage(SendMessage sendMessage) {
        Sender.sendMessage = sendMessage;
    }
}
