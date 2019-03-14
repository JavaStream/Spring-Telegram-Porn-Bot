package com.javastream;

import com.javastream.commands.Find;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Created by Serg on 14.03.2019.
 */
public class StateFinder {

    private Message message;


   public void getMethodOfEvent(String name, Message message) {
       this.message=message;

       if (name.equals("FIND")) {
           findMethod();
       }
   }

   public void findMethod() {
       new Find().findCommand(message);
   }
}
