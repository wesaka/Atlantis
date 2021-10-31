package atlantis.production.dynamic;

import atlantis.AGame;
import atlantis.AtlantisConfig;
import atlantis.production.constructing.AConstructionRequests;
import atlantis.production.orders.AddToQueue;
import atlantis.units.AUnit;
import atlantis.units.AUnitType;
import atlantis.units.select.Count;
import atlantis.units.select.Select;
import atlantis.util.Helpers;


public abstract class ADynamicBuildingsManager extends Helpers {

    public static void update() {
        
        // Check if we should automatically build new base, because we have shitload of minerals.
        AExpansionManager.requestNewBaseIfNeeded();
        
        // If number of bases is bigger than gas buildings, it usually makes sense to build new gas extractor
        gasBuildingIfNeeded();
    }
    
    // =========================================================

    /**
     * Build Refineries/Assimilators/Extractors when it makes sense.
     */
    private static void gasBuildingIfNeeded() {
        if (AGame.supplyTotal() <= 20) {
            return;
        }

        if (AGame.everyNthGameFrame(35)) {
            return;
        }
        
        // =========================================================
        
        int numberOfBases = Select.ourBases().count();
        int numberOfGasBuildings = Select.ourIncludingUnfinished().ofType(AtlantisConfig.GAS_BUILDING).count();
        if (
            numberOfBases >= 2
            && numberOfBases > numberOfGasBuildings && !AGame.canAfford(0, 350)
            && AConstructionRequests.countNotStartedConstructionsOfType(AtlantisConfig.GAS_BUILDING) == 0
            && hasABaseWithFreeGeyser()
        ) {
            AddToQueue.addWithTopPriority(AtlantisConfig.GAS_BUILDING);
        }
    }

    // =========================================================

    protected static void buildToHaveOne(int minSupply, AUnitType type) {
        if (AGame.supplyUsed() >= minSupply) {
            buildToHaveOne(type);
        }
    }

    protected static void buildToHaveOne(AUnitType type) {
        if (Count.ofType(type) > 0) {
            return;
        }

        buildNow(type, true);
    }

    protected static void buildIfCanAfford(AUnitType type) {
        buildIfCanAfford(type, true, type.getMineralPrice(), type.getGasPrice());
    }

    protected static void buildIfCanAffordWithReserved(AUnitType type) {
        buildIfCanAffordWithReserved(type, true, type.getMineralPrice(), type.getGasPrice());
    }

    protected static void buildIfAllBusyButCanAfford(AUnitType type, int extraMinerals, int extraGas) {
        if (Select.ourOfType(type).areAllBusy()) {
            buildIfCanAfford(type, true, type.getMineralPrice() + extraMinerals, type.getGasPrice() + extraGas);
        }
    }

    protected static void buildIfCanAfford(AUnitType type, boolean onlyOneAtTime, int hasMinerals, int hasGas) {
        if (!AGame.canAfford(hasMinerals, hasGas)) {
            return;
        }

        buildNow(type, onlyOneAtTime);
    }

    protected static void buildIfCanAffordWithReserved(AUnitType type, boolean onlyOneAtTime, int hasMinerals, int hasGas) {
        if (!AGame.canAffordWithReserved(hasMinerals, hasGas)) {
            return;
        }

        buildNow(type, onlyOneAtTime);
    }

    protected static void buildNow(AUnitType type, boolean onlyOneAtTime) {
        if (onlyOneAtTime && AConstructionRequests.hasRequestedConstructionOf(type)) {
            return;
        }

        if (!hasRequiredUnitFor(type)) {
            buildToHaveOne(type.getWhatIsRequired());
            return;
        }

        AddToQueue.addWithTopPriority(type);
    }

    // =========================================================

    public static boolean hasABaseWithFreeGeyser() {
        for (AUnit base : Select.ourBases().listUnits()) {
            if (Select.geysers().inRadius(8, base).isNotEmpty()) {
                return true;
            }
        }

        return false;
    }

}