package atlantis.log;

import atlantis.production.constructing.ConstructionOrder;
import atlantis.production.constructing.ConstructionRequests;
import atlantis.util.A;

import java.util.ArrayList;
import java.util.Iterator;

public class ALog {

    private static ArrayList<LogMessage> messages = new ArrayList<>();

    public static void addMessage(String message) {
        messages.add(new LogMessage(message));
    }

    public static ArrayList<LogMessage> messages() {
        if (A.everyNthGameFrame(60)) {
            removeOldMessages();
        }

        return messages;
    }

    private static void removeOldMessages() {
        messages.removeIf(LogMessage::expired);
    }

}
