package communication;

import jade.core.Agent;

public class CommunicatorAgent extends Agent {
    protected void setup() {
        if (Utils.register(this, Utils.COMMUNICATOR_AGENT_NAME))
            System.out.println(
                    "Communicator Agent loaded successfully."
            );
    }
}
