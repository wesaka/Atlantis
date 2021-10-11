package atlantis.production;

import atlantis.AGame;
import atlantis.constructing.AConstructionRequests;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.Select;

public class AbstractDynamicUnits {

    protected static void trainIfPossible(int minSupply, AUnitType unitType, boolean onlyOneAtTime) {
        if (AGame.getSupplyUsed() < minSupply) {
            return;
        }

        trainIfPossible(unitType, onlyOneAtTime, unitType.getMineralPrice(), unitType.getGasPrice());
    }

    protected static void trainIfPossible(AUnitType unitType, boolean onlyOneAtTime, int hasMinerals, int hasGas) {
        if (!AGame.canAfford(hasMinerals, hasGas)) {
            return;
        }

        if (onlyOneAtTime && AConstructionRequests.hasRequestedConstructionOf(unitType)) {
            return;
        }

        AUnitType building = unitType.getWhatBuildsIt();
        for (AUnit buildingProducing : Select.ourOfType(building).listUnits()) {
            if (!buildingProducing.isTrainingAnyUnit()) {
                buildingProducing.train(unitType);
                return;
            }
        }
    }

}
