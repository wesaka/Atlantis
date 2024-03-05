package communication;

import jade.core.Agent;
import utils.Utils;

public class CommunicatorAgent extends Agent {
    protected void setup() {
        String COMMUNICATOR_AGENT_NAME = "BotCommunicatorAgent";

        if (Utils.register(this, COMMUNICATOR_AGENT_NAME))
            System.out.println(
                    "Communicator Agent loaded successfully."
            );
    }
}
