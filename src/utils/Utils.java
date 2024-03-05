package utils;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

// We will slowly cannibalize the Utils from the project we had done
public final class Utils {

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
