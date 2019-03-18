package com.javastream.service;

/**
 * Файл настроек системы
 */
public interface Properties {


    // Базовый сайт, на котором производится поиск контента
    String URL = "https://www.xnxx.com";

    // Директория сохранения скаченных MP4 файлов на диске
    String LOCATION = "d://";

    // Стартовое количество видео, выдаваемых клиенту на запрос /find
    int NUMBER_OF_VIDEOS_START = 5;

    // Количество видео, выдаваемых порционно клиенту на запрос /more  после того как сформирован массив видео по ключевому запросу
    int NUMBER_OF_VIDEOS_MORE = 4;


}