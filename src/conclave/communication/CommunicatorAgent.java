package conclave.communication;

import conclave.Utils;
import jade.core.Agent;

/**
 * This is the class where the communication with conclave is going to be made
 * <p>
 * We're going to inform the ministers on what is going on
 * How? Broadcast the information I guess
 * TODO Gotta implement a robust Publish-Subscribe pattern
 */
public class CommunicatorAgent extends Agent {
    protected void setup() {
        if (Utils.register(this, Utils.COMMUNICATOR_AGENT_NAME))
            System.out.println(
                    "Communicator Agent loaded successfully."
            );
    }
}
