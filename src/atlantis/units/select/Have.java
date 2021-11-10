package atlantis.units.select;

import atlantis.position.APosition;
import atlantis.position.HasPosition;
import atlantis.production.constructing.AConstructionRequests;
import atlantis.production.orders.ProductionQueue;
import atlantis.units.AUnitType;

public class Have {

    public static boolean armory() {
        return Count.ofType(AUnitType.Terran_Armory) > 0;
    }

    public static boolean base() {
        return Select.main() != null;
    }

    public static boolean engBay() {
        return Count.ofType(AUnitType.Terran_Engineering_Bay) > 0;
    }

    public static boolean barracks() {
        return Count.ofType(AUnitType.Terran_Barracks) > 0;
    }

    public static boolean existingOrPlanned(AUnitType building, HasPosition position, double inRadius) {
        assert building.isBuilding();

        if (AConstructionRequests.hasNotStartedConstructionNear(building, position, inRadius)) {
            return true;
        }

        return Select.ourOfTypeIncludingUnfinished(building).inRadius(inRadius, position).atLeast(1);
    }

    public static boolean existingOrPlannedOrInQueue(AUnitType building, HasPosition position, double inRadius) {
        assert building.isBuilding();

        if (ProductionQueue.isAtTheTopOfQueue(building, 2)) {
            return true;
        }

        if (AConstructionRequests.hasNotStartedConstructionNear(building, position, inRadius)) {
            return true;
        }

        return Select.ourOfTypeIncludingUnfinished(building).inRadius(inRadius, position).atLeast(1);
    }

    public static boolean main() {
        return base();
    }
}
