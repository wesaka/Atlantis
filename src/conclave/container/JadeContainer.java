package conclave.container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class JadeContainer {
    private static AgentContainer cc;

    public static final boolean log = false;

    // Executed from Singletons.
    public static void loadBoot() {

        // Get a hold on JADE runtime
        jade.core.Runtime rt = jade.core.Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);
        System.out.println("Runtime created");

        // Create a default profile
        Profile profile = new ProfileImpl(null, 1200, null);
        //profile.setParameter("verbosity","5");
        System.out.println("Profile created");

        System.out.println("Launching a whole in-process platform..." + profile);


        try {
            cc = rt.createMainContainer(profile);

            System.out.println("Launching the rma agent on the main container ...");
            cc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]).start();

        } catch (StaleProxyException e) {
            System.err.println("Error during boot!!!");
            e.printStackTrace();
            System.exit(1);
        }

        // now set the default Profile to start a container
        ProfileImpl agentContainerProfile = new ProfileImpl(null, 1200, null);
        //System.out.println("Launching the agent container ..."+agentContainerProfile);

        cc = rt.createAgentContainer(agentContainerProfile);
        //System.out.println("Launching the agent container after ..."+agentContainerProfile);
        System.out.println("Agent Container created");
    }

    private static void loadAgents() {
        try {
            // TODO Instantiate the Bot Communicator
            //cc.createNewAgent(ID_PHASE_MANAGER, PhaseManager.class.getName(), new Object[]{}).start();

        } catch (Exception e) {
            System.err.println("Error creating agent!!!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadSniffer() {
        try {
            cc.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new Object[0]).start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
