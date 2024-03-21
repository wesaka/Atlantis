package conclave;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Utils {
    // Define the names for the agents here
    public static final String COMMUNICATOR_AGENT_NAME = "BotCommunicatorAgent";

    // Configs
    //private static final Codec codec = new SLCodec();
    // todo define the ontologies we are going to be using
    //private static final Ontology ontology = WoAOntology.getInstance();

    /**
     * Returns true if the agent is registered in the DFService.
     *
     * @param agent the agent.
     * @param type  the service.
     * @return true if the agent is registered, false otherwise.
     */
    public static boolean register(Agent agent, String type) {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(agent.getLocalName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        dfd.addServices(sd);

        try {
            DFService.register(agent, dfd);
            return true;
        } catch (FIPAException fe) {
            fe.printStackTrace();
            return false;
        }
    }

    /**
     * Deregister the agent from all attached services.
     *
     * @param agent the agent.
     */
    public static void deregister(Agent agent) {
        try {
            DFService.deregister(agent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
