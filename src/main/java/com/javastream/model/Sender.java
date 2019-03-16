package com.javastream.model;

import com.javastream.service.SendTextMsg;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.ArrayList;


public class Sender {

    private static String text;
    private static Message message;
    private static ArrayList<SendPhoto> arrayListSendPhoto;
    private static boolean exsistArray;
    private static Videos videos;

    public Sender() {

    }

    public static SendMessage sendMessage() {
            return new SendTextMsg().sendTextMsg(Sender.getMessage(), Sender.getText());
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

    public static boolean getExsistArray() {
        return exsistArray;
    }

    public static void setExsistArray(boolean isExsistArray) {
        Sender.exsistArray = isExsistArray;
    }

    public static Videos getVideos() {
        return videos;
    }

    public static void setVideos(Videos videos) {
        Sender.videos = videos;
    }
}
