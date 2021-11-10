package atlantis.production.constructing;

import atlantis.AGame;
import atlantis.position.HasPosition;
import atlantis.production.constructing.position.AbstractPositionFinder;
import atlantis.position.APosition;
import atlantis.production.ProductionOrder;
import atlantis.production.Requirements;
import atlantis.production.orders.ProductionQueue;
import atlantis.production.orders.ProductionQueueRebuilder;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Count;
import atlantis.units.select.Select;
import atlantis.util.We;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AConstructionRequests {

    /**
     * List of all unfinished (started or pending) constructions.
     */
    protected static ConcurrentLinkedQueue<ConstructionOrder> constructionOrders = new ConcurrentLinkedQueue<>();

    /**
     * Issues request of constructing new building. It will automatically find position and builder unit for
     * it.
     */
    public static void requestConstructionOf(AUnitType building) {
        requestConstructionOf(building, null, null);
    }

    public static boolean requestConstructionOf(ProductionOrder order) {
        return requestConstructionOf(order.unitType(), order.atPosition(), order);
    }

    /**
     * Find a place for new building and assign a worker.
     *
     * WARNING: passed order parameter can later override nearTo parameter.
     */
    public static boolean requestConstructionOf(AUnitType building, HasPosition near, ProductionOrder order) {

        // Validate
        if (!building.isBuilding()) {
            throw new RuntimeException("Requested construction of not building!!! Type: " + building);
        }

        if (ASpecificConstructionManager.handledAsSpecialBuilding(building, order)) {
            return true;
        }

        if (!Requirements.hasRequirements(building)) {
            if (We.protoss() && AGame.supplyTotal() <= 10) {
                return false;
            }

            AUnitType requiredBuilding = building.getWhatIsRequired();
            if (countExistingAndNotFinished(requiredBuilding) == 0) {
                AConstructionRequests.requestConstructionOf(requiredBuilding);
                return true;
            }

            System.err.println("Uhmmm... shouldn't reach here.");
            System.err.println(building + " // " + requiredBuilding);
            return false;
        }

        // =========================================================
        // Create ConstructionOrder object, assign random worker for the time being

        ConstructionOrder newConstructionOrder = new ConstructionOrder(building);
        newConstructionOrder.setProductionOrder(order);
        newConstructionOrder.setNearTo(near);
        newConstructionOrder.assignRandomBuilderForNow();

        if (newConstructionOrder.getBuilder() == null) {
            if (AGame.supplyUsed() >= 7 && Count.bases() > 0 && Count.workers() > 0) {
                System.err.println("Builder is null, got damn it!");
            }
            return false;
        }

        // =========================================================
        // Find place for new building

        newConstructionOrder.setMaxDistance(-1);
        APosition positionToBuild = newConstructionOrder.findPositionForNewBuilding();

        // =========================================================
        // Successfully found position for new building

        if (positionToBuild != null) {

            // Update construction order with found position for building
            newConstructionOrder.setPositionToBuild(positionToBuild);

            // Assign optimal builder for this building
            newConstructionOrder.assignOptimalBuilder();

            // Add to list of pending orders
            constructionOrders.add(newConstructionOrder);

            // Rebuild production queue as new building is about to be built
            ProductionQueueRebuilder.rebuildProductionQueueToExcludeProducedOrders();

            return true;
        }

        // Couldn't find place for building! That's bad, print descriptive explanation.
        else {
            System.err.print("Construction place failed for `" + building + "`  ");
            if (AbstractPositionFinder._CONDITION_THAT_FAILED != null) {
                System.err.print("(reason: " + AbstractPositionFinder._CONDITION_THAT_FAILED + ")");
            } else {
                System.err.print("(reason was not properly defined)");
            }
            System.err.println();

            newConstructionOrder.cancel();
            return false;
        }
    }

    /**
     * Returns ConstructionOrder object for given builder.
     */
    public static ConstructionOrder getConstructionOrderFor(AUnit builder) {
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (builder.equals(constructionOrder.getBuilder())) {
                return constructionOrder;
            }
        }

        return null;
    }

    public static boolean hasRequestedConstructionOf(AUnitType type) {
        return countNotFinishedConstructionsOfType(type) > 0;
    }

    /**
     * If we requested to build building A and even assigned worker who's travelling to the building site,
     * it's still doesn't count as unitCreated. We need to manually count number of constructions and only
     * then, we can e.g. "count unstarted barracks constructions".
     */
    public static int countNotStartedConstructionsOfType(AUnitType type) {
        int total = 0;
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED
                    && constructionOrder.getBuildingType().equals(type)) {
                total++;
            }
        }

        // =========================================================
        // Special case for Overlord
        if (type.equals(AUnitType.Zerg_Overlord)) {
            total += Select.ourUnfinished().ofType(type).count();
        }

        return total;
    }

    public static int countNotStartedConstructionsOfTypeInRadius(AUnitType type, double radius, APosition position) {
        int total = 0;
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED
                    && constructionOrder.getBuildingType().equals(type)
                    && position.distTo(constructionOrder.getPositionToBuildCenter()) <= radius) {
                total++;
            }
        }

        // =========================================================
        // Special case for Overlord
        if (type.equals(AUnitType.Zerg_Overlord)) {
            total += Select.ourUnfinished().ofType(type).count();
        }

        return total;
    }

    public static int countNotFinishedConstructionsOfType(AUnitType type) {
        return Select.ourUnfinished().ofType(type).count()
                + countNotStartedConstructionsOfType(type);
    }

    public static int countNotFinishedConstructionsOfTypeInRadius(AUnitType type, double radius, APosition position) {
        return Select.ourUnfinished().ofType(type).inRadius(radius, position).count()
                + countNotStartedConstructionsOfTypeInRadius(type, radius, position);
    }

    /**
     * Returns how many buildings (or Overlords) of given type are currently being produced (started, but not
     * finished).
     */
    public static int countPendingConstructionsOfType(AUnitType type) {
        int total = 0;
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_IN_PROGRESS
                    && constructionOrder.getBuildingType().equals(type)) {
                total++;
            }
        }

        // =========================================================
        // Special case for Overlord
        if (type.equals(AUnitType.Zerg_Overlord)) {
            total += Select.ourUnfinished().ofType(AUnitType.Zerg_Overlord).count();
        }

        return total;
    }

    public static int countExistingAndExpectedInNearFuture(AUnitType type, int amongNTop) {
        if (!type.isBuilding()) {
            throw new RuntimeException("Can only use it for buildings: " + type);
        }

        return Select.ourOfType(type).count()
                + ProductionQueue.countInQueue(type, amongNTop);
    }

    public static int countExistingAndNotFinished(AUnitType type) {
        if (!type.isBuilding()) {
            throw new RuntimeException("Can only use it for buildings: " + type);
        }

        return Select.ourOfTypeIncludingUnfinished(type).count()
                + countNotFinishedConstructionsOfType(type);
    }

    public static int countExistingAndPlannedConstructionsInRadius(AUnitType type, double radius, APosition position) {
        if (!type.isBuilding()) {
            throw new RuntimeException("Can only use it for buildings: " + type);
        }

        return Select.ourOfTypeIncludingUnfinished(type).inRadius(radius, position).count()
                + countNotFinishedConstructionsOfTypeInRadius(type, radius, position);
    }

    /**
     * If we requested to build building A and even assigned worker who's travelling to the building site,
     * it's still doesn't count as unitCreated. We need to manually count number of constructions and only
     * then, we can e.g. "get not started Terran Barracks constructions".
     */
    public static ArrayList<ConstructionOrder> getNotStartedConstructionsOfType(AUnitType type) {
        ArrayList<ConstructionOrder> notStarted = new ArrayList<>();
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED
                    && (type == null || constructionOrder.getBuildingType().equals(type))) {
                notStarted.add(constructionOrder);
            }
        }
        return notStarted;
    }

    public static ArrayList<ConstructionOrder> getNotStartedConstructions() {
        ArrayList<ConstructionOrder> notStarted = new ArrayList<>();
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED) {
                notStarted.add(constructionOrder);
            }
        }
        return notStarted;
    }

    /**
     * Returns every construction order that is active in this moment. It will include even those buildings
     * that haven't been started yet.
     *
     * @return
     */
    public static ArrayList<ConstructionOrder> getAllConstructionOrders() {
        return new ArrayList<>(constructionOrders);
    }

    /**
     * @return first int is number minerals, second int is number of gas required.
     */
    public static int[] resourcesNeededForNotStartedConstructions() {
        int mineralsNeeded = 0;
        int gasNeeded = 0;
        for (ConstructionOrder constructionOrder : getNotStartedConstructionsOfType(null)) {
            mineralsNeeded += constructionOrder.getBuildingType().getMineralPrice();
            gasNeeded += constructionOrder.getBuildingType().getGasPrice();
        }
        int[] result = {mineralsNeeded, gasNeeded};
        return result;
    }

    protected static void removeOrder(ConstructionOrder constructionOrder) {
        constructionOrders.remove(constructionOrder);
    }

    /**
     * Top-priority request.
     */
    public static void removeAllNotStarted() {
        for (Iterator<ConstructionOrder> iterator = AConstructionRequests.constructionOrders.iterator(); iterator.hasNext(); ) {
            ConstructionOrder constructionOrder =  iterator.next();
            if (!constructionOrder.hasStarted()) {
                constructionOrder.cancel();
            }
        }
    }

    public static boolean hasNotStartedConstructionNear(AUnitType building, HasPosition position, double inRadius) {
        for (ConstructionOrder order : getNotStartedConstructionsOfType(building)) {
            if (order.positionToBuild() != null && position.distToLessThan(position, inRadius)) {
                return true;
            }
        }
        return false;
    }
}
